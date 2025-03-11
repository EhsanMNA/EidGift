package me.ehsanmna.eidGift.commands;

import me.ehsanmna.eidGift.EidGift;
import me.ehsanmna.eidGift.api.events.GiftClaimEvent;
import me.ehsanmna.eidGift.config.MessageManager;
import me.ehsanmna.eidGift.config.ParticleConfig;
import me.ehsanmna.eidGift.items.GiftItem;
import me.ehsanmna.eidGift.items.GiftManager;
import me.ehsanmna.eidGift.utils.ClaimManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClaimKadoCommand implements CommandExecutor {
    private final EidGift plugin;
    private final GiftManager giftManager;
    private final ClaimManager claimManager;
    private final MessageManager messageManager;

    public ClaimKadoCommand(EidGift plugin, GiftManager giftManager, ClaimManager claimManager, MessageManager messageManager) {
        this.plugin = plugin;
        this.giftManager = giftManager;
        this.claimManager = claimManager;
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messageManager.getMessage("player-only"));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("eidgift.claim")) {
            player.sendMessage(messageManager.getMessage("no-permission"));
            return true;
        }

        List<String> claims = claimManager.getClaims(player.getName());
        if (claims.isEmpty()) {
            player.sendMessage(messageManager.getMessage("no-claims"));
            return true;
        }

        openClaimGUI(player, claims);
        return true;
    }

    private void openClaimGUI(Player player, List<String> claims) {
        int size = Math.min((int) Math.ceil((claims.size() + 1) / 9.0) * 9, 54);
        Inventory gui = Bukkit.createInventory(player, size, messageManager.getMessage("claim-gui-title"));

        ItemStack border = createBorderItem();
        for (int i = 0; i < size; i++) {
            if (i < 9 || i >= size - 9 || i % 9 == 0 || i % 9 == 8) {
                gui.setItem(i, border);
            }
        }

        int slot = 10;
        for (String claim : claims) {
            String[] parts = claim.split(";");
            String giftId = parts[0];
            LocalDateTime timestamp = LocalDateTime.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            GiftItem gift = giftManager.getGift(giftId);
            if (gift != null && gift.isActive() && !isExpired(gift, timestamp)) {
                ItemStack preview = gift.isWrapped() ? giftManager.getWrapper(gift.getWrappingItem()).createItem(player.getName()) : gift.createItem(player.getName());
                gui.setItem(slot, preview);
                slot++;
                if (slot % 9 == 8) slot += 2;
            }
        }

        ItemStack claimAll = createClaimAllItem();
        gui.setItem(size - 5, claimAll);

        player.openInventory(gui);
    }

    private ItemStack createBorderItem() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§7 ");
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createClaimAllItem() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(messageManager.getMessage("claim-all-button"));
        item.setItemMeta(meta);
        return item;
    }

    public void handleClaimClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!event.getView().getTitle().equals(messageManager.getMessage("claim-gui-title"))) return;

        event.setCancelled(true);
        int slot = event.getSlot();
        List<String> claims = claimManager.getClaims(player.getName());
        Inventory gui = event.getInventory();

        if (slot == gui.getSize() - 5) {
            claimAll(player, claims);
            player.closeInventory();
            return;
        }

        int claimIndex = slotToClaimIndex(slot, claims.size());
        if (claimIndex < 0 || claimIndex >= claims.size()) return;

        String claim = claims.get(claimIndex);
        String[] parts = claim.split(";");
        String giftId = parts[0];
        LocalDateTime timestamp = LocalDateTime.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        GiftItem gift = giftManager.getGift(giftId);
        if (gift == null || !gift.isActive()) {
            player.sendMessage(messageManager.getMessage("gift-unavailable").replace("%gift%", giftId));
            return;
        }
        if (isExpired(gift, timestamp)) {
            player.sendMessage(messageManager.getMessage("gift-expired").replace("%gift%", giftId));
            claims.remove(claimIndex);
            updateClaims(player.getName(), claims);
            return;
        }

        ItemStack item = gift.isWrapped() ? giftManager.getWrapper(gift.getWrappingItem()).createItem(player.getName()) : gift.createItem(player.getName());
        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(messageManager.getMessage("inventory-full"));
            return;
        }

        GiftClaimEvent claimEvent = new GiftClaimEvent(player, gift);
        Bukkit.getPluginManager().callEvent(claimEvent);
        if (claimEvent.isCancelled()) return;

        player.getInventory().addItem(item);
        playEffects(player, gift);
        player.sendMessage(messageManager.getMessage("gift-claimed").replace("%gift%", giftId));

        claims.remove(claimIndex);
        updateClaims(player.getName(), claims);

        player.closeInventory();
        openClaimGUI(player, claims);
    }

    private void claimAll(Player player, List<String> claims) {
        for (String claim : claims.toArray(new String[0])) {
            String[] parts = claim.split(";");
            String giftId = parts[0];
            LocalDateTime timestamp = LocalDateTime.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            GiftItem gift = giftManager.getGift(giftId);
            if (gift == null || !gift.isActive()) {
                player.sendMessage(messageManager.getMessage("gift-unavailable").replace("%gift%", giftId));
                continue;
            }
            if (isExpired(gift, timestamp)) {
                player.sendMessage(messageManager.getMessage("gift-expired").replace("%gift%", giftId));
                continue;
            }

            ItemStack item = gift.isWrapped() ? giftManager.getWrapper(gift.getWrappingItem()).createItem(player.getName()) : gift.createItem(player.getName());
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(messageManager.getMessage("inventory-full"));
                return;
            }

            GiftClaimEvent claimEvent = new GiftClaimEvent(player, gift);
            Bukkit.getPluginManager().callEvent(claimEvent);
            if (claimEvent.isCancelled()) continue;

            player.getInventory().addItem(item);
            playEffects(player, gift);
            player.sendMessage(messageManager.getMessage("gift-claimed").replace("%gift%", giftId));
        }

        plugin.getConfigManager().getData().set("claims." + player.getName(), null);
        claimManager.saveClaims();
        player.sendMessage(messageManager.getMessage("claimed-all"));
    }

    private boolean isExpired(GiftItem gift, LocalDateTime timestamp) {
        if (gift.getExpirationHours() == 0) return false;
        return LocalDateTime.now().isAfter(timestamp.plusHours(gift.getExpirationHours()));
    }

    private void updateClaims(String playerName, List<String> claims) {
        if (claims.isEmpty()) {
            plugin.getConfigManager().getData().set("claims." + playerName, null);
        } else {
            plugin.getConfigManager().getData().set("claims." + playerName, claims);
        }
        claimManager.saveClaims();
    }

    private int slotToClaimIndex(int slot, int claimSize) {
        if (slot < 10 || slot >= 54 - 9) return -1;
        int row = slot / 9;
        int col = slot % 9;
        if (col == 0 || col == 8) return -1;
        return (row - 1) * 7 + (col - 1);
    }

    private void playEffects(Player player, GiftItem gift) {
        if (gift.getSound() != null) {
            player.playSound(player.getLocation(), Sound.valueOf(gift.getSound()), 1.0f, 1.0f);
        }
        if (gift.getParticle() != null) {
            ParticleConfig particleConfig = new ParticleConfig(plugin.getConfigManager().getParticles().getConfigurationSection(gift.getParticle()));
            player.spawnParticle(particleConfig.getType(), player.getLocation(), particleConfig.getCount());
        }
    }
}