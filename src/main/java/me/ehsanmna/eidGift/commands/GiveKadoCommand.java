package me.ehsanmna.eidGift.commands;

import me.ehsanmna.eidGift.EidGift;
import me.ehsanmna.eidGift.api.events.GiftGiveEvent;
import me.ehsanmna.eidGift.config.MessageManager;
import me.ehsanmna.eidGift.config.ParticleConfig;
import me.ehsanmna.eidGift.items.GiftItem;
import me.ehsanmna.eidGift.items.GiftManager;
import me.ehsanmna.eidGift.utils.ClaimManager;
import me.ehsanmna.eidGift.utils.CustomizationState;
import me.ehsanmna.eidGift.utils.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GiveKadoCommand implements CommandExecutor, TabCompleter {
    private final EidGift plugin;
    private final GiftManager giftManager;
    private final ClaimManager claimManager;
    private final EconomyManager economyManager;
    private final MessageManager messageManager;
    private final Map<Player, CustomizationState> customizationStates = new HashMap<>();

    public GiveKadoCommand(EidGift plugin, GiftManager giftManager, ClaimManager claimManager, EconomyManager economyManager, MessageManager messageManager) {
        this.plugin = plugin;
        this.giftManager = giftManager;
        this.claimManager = claimManager;
        this.economyManager = economyManager;
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("eidgift.givekado")) {
            sender.sendMessage(messageManager.getMessage("no-permission"));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("eidgift.reload")) {
                sender.sendMessage(messageManager.getMessage("no-reload-permission"));
                return true;
            }
            giftManager.reloadGifts();
            sender.sendMessage(messageManager.getMessage("reload-success"));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(messageManager.getMessage("player-only"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(messageManager.getMessage("usage"));
            return true;
        }

        Player player = (Player) sender;
        String targetName = args[0];
        String giftId = args[1].toLowerCase();

        GiftItem gift = giftManager.getGift(giftId);
        if (gift == null) {
            sender.sendMessage(messageManager.getMessage("gift-not-found").replace("%gift%", giftId));
            return true;
        }

        if (!gift.isActive()) {
            sender.sendMessage(messageManager.getMessage("gift-unavailable").replace("%gift%", giftId));
            return true;
        }

        if (args.length > 2 && args[2].equalsIgnoreCase("customize")) {
            openCustomizationGUI(player, gift, targetName);
            return true;
        }

        giveGift(player, targetName, gift);
        return true;
    }

    public void openCustomizationGUI(Player player, GiftItem gift, String targetName) {
        Inventory gui = Bukkit.createInventory(player, 27, messageManager.getMessage("customize-gui-title"));
        ItemStack nameItem = new ItemStack(Material.NAME_TAG);
        ItemMeta nameMeta = nameItem.getItemMeta();
        nameMeta.setDisplayName(messageManager.getMessage("customize-name"));
        nameItem.setItemMeta(nameMeta);

        ItemStack loreItem = new ItemStack(Material.BOOK);
        ItemMeta loreMeta = loreItem.getItemMeta();
        loreMeta.setDisplayName(messageManager.getMessage("customize-lore"));
        loreItem.setItemMeta(loreMeta);

        ItemStack confirmItem = new ItemStack(Material.EMERALD);
        ItemMeta confirmMeta = confirmItem.getItemMeta();
        confirmMeta.setDisplayName(messageManager.getMessage("customize-confirm"));
        confirmItem.setItemMeta(confirmMeta);

        gui.setItem(11, nameItem);
        gui.setItem(13, loreItem);
        gui.setItem(15, confirmItem);

        customizationStates.put(player, new CustomizationState(gift, targetName));
        player.openInventory(gui);
    }

    public void giveGift(Player player, String targetName, GiftItem gift) {
        if (gift.getCost() > 0 && !economyManager.hasEnough(player, gift.getCost())) {
            player.sendMessage(messageManager.getMessage("not-enough-money").replace("%cost%", String.valueOf(gift.getCost())));
            return;
        }

        Player target = Bukkit.getPlayer(targetName);
        ItemStack item = gift.isWrapped() ? giftManager.getWrapper(gift.getWrappingItem()).createItem(targetName) : gift.createItem(targetName);
        GiftGiveEvent giveEvent = new GiftGiveEvent(player, target, gift);
        Bukkit.getPluginManager().callEvent(giveEvent);
        if (giveEvent.isCancelled()) return;

        if (target == null || !target.isOnline()) {
            claimManager.addClaim(targetName, gift.getId());
            player.sendMessage(messageManager.getMessage("gift-queued-offline").replace("%player%", targetName));
            sendNotification(targetName);
            if (gift.getCost() > 0) economyManager.withdraw(player, gift.getCost());
            playWrapAnimation(player, gift);
        } else if (target.getInventory().firstEmpty() == -1) {
            claimManager.addClaim(targetName, gift.getId());
            player.sendMessage(messageManager.getMessage("gift-queued-full").replace("%player%", targetName));
            sendNotification(targetName);
            if (gift.getCost() > 0) economyManager.withdraw(player, gift.getCost());
            playWrapAnimation(player, gift);
        } else {
            target.getInventory().addItem(item);
            player.sendMessage(messageManager.getMessage("gift-given").replace("%gift%", gift.getId()).replace("%player%", targetName));
            target.sendMessage(messageManager.getMessage("gift-received").replace("%player%", player.getName()));
            playEffects(target, gift);
            sendNotification(targetName);
            if (gift.getCost() > 0) economyManager.withdraw(player, gift.getCost());
            playWrapAnimation(player, gift);
        }
        customizationStates.remove(player);
    }

    private void sendNotification(String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target != null) {
            String type = plugin.getConfigManager().getConfig().getString("notification.type", "TITLE");
            String message = messageManager.getMessage("notification-message");
            switch (type.toUpperCase()) {
                case "TITLE":
                    target.sendTitle(message, "", 10, 70, 20);
                    break;
                case "ACTIONBAR":
                    target.sendMessage(message); // Fallback for compatibility
                    break;
                case "CHAT":
                    target.sendMessage(message);
                    break;
            }
        }
    }

    private void playWrapAnimation(Player player, GiftItem gift) {
        if (gift.getWrapSound() != null) {
            player.playSound(player.getLocation(), Sound.valueOf(gift.getWrapSound()), 1.0f, 1.0f);
        }
        if (gift.getWrapParticle() != null) {
            ParticleConfig particleConfig = new ParticleConfig(plugin.getConfigManager().getParticles().getConfigurationSection(gift.getWrapParticle()));
            player.spawnParticle(particleConfig.getType(), player.getLocation(), particleConfig.getCount());
        }
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (!sender.hasPermission("eidgift.givekado")) return completions;

        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>(Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList()));
            if (sender.hasPermission("eidgift.reload")) suggestions.add("reload");
            completions.addAll(suggestions.stream()
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .sorted()
                    .collect(Collectors.toList()));
        } else if (args.length == 2 && !args[0].equalsIgnoreCase("reload")) {
            completions.addAll(giftManager.getGiftIds().stream()
                    .filter(id -> id.toLowerCase().startsWith(args[1].toLowerCase()))
                    .sorted()
                    .collect(Collectors.toList()));
        } else if (args.length == 3 && !args[0].equalsIgnoreCase("reload")) {
            completions.add("customize");
        }

        return completions;
    }

    public Map<Player, CustomizationState> getCustomizationStates() {
        return customizationStates;
    }
}