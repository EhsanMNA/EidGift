package me.ehsanmna.eidGift.listeners;

import me.ehsanmna.eidGift.EidGift;
import me.ehsanmna.eidGift.commands.GiveKadoCommand;
import me.ehsanmna.eidGift.config.MessageManager;
import me.ehsanmna.eidGift.items.GiftItem;
import me.ehsanmna.eidGift.utils.CustomizationState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiftCustomizeListener implements Listener {
    private final EidGift plugin;
    private final GiveKadoCommand giveCommand;
    private final MessageManager messageManager;

    public GiftCustomizeListener(EidGift plugin) {
        this.plugin = plugin;
        this.giveCommand = new GiveKadoCommand(plugin, plugin.getGiftManager(), plugin.getClaimManager(), new me.ehsanmna.eidGift.utils.EconomyManager(plugin), plugin.getMessageManager());
        this.messageManager = plugin.getMessageManager();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!event.getView().getTitle().equals(messageManager.getMessage("customize-gui-title"))) return;

        event.setCancelled(true);
        int slot = event.getSlot();
        CustomizationState state = giveCommand.getCustomizationStates().get(player);
        if (state == null) return;

        if (slot == 11) { // Name customization
            openAnvilGUI(player, "Name", state);
        } else if (slot == 13) { // Lore customization
            openAnvilGUI(player, "Lore", state);
        } else if (slot == 15) { // Confirm
            GiftItem gift = state.gift;
            if (state.customName != null) gift.setCustomization("name", state.customName);
            if (state.customLore != null) gift.setCustomization("lore", state.customLore);
            giveCommand.giveGift(player, state.targetName, gift);
            player.closeInventory();
        }
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        Player player = (Player) event.getViewers().get(0);
        String title = event.getView().getTitle();
        CustomizationState state = giveCommand.getCustomizationStates().get(player);
        if (state == null || (!title.equals("Customize Name") && !title.equals("Customize Lore"))) return;

        AnvilInventory anvil = event.getInventory();
        ItemStack input = anvil.getItem(0);
        if (input != null && input.getType() == Material.PAPER) {
            String result = anvil.getRenameText();
            if (result != null && !result.isEmpty()) {
                ItemStack output = new ItemStack(Material.PAPER);
                ItemMeta meta = output.getItemMeta();
                meta.setDisplayName(result);
                output.setItemMeta(meta);
                event.setResult(output);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inv = event.getInventory();
        if (!(inv instanceof AnvilInventory)) return;

        AnvilInventory anvil = (AnvilInventory) inv;
        String title = event.getView().getTitle();
        CustomizationState state = giveCommand.getCustomizationStates().get(player);
        if (state == null || (!title.equals("Customize Name") && !title.equals("Customize Lore"))) return;

        ItemStack result = anvil.getItem(2);
        if (result != null && result.getType() == Material.PAPER) {
            String text = result.getItemMeta().getDisplayName();
            if (title.equals("Customize Name")) {
                state.customName = text;
            } else if (title.equals("Customize Lore")) {
                state.customLore = text;
            }
        }

        giveCommand.openCustomizationGUI(player, state.gift, state.targetName);
    }

    private void openAnvilGUI(Player player, String type, CustomizationState state) {
        Inventory anvil = Bukkit.createInventory(player, org.bukkit.event.inventory.InventoryType.ANVIL, "Customize " + type);
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName(type.equals("Name") ? state.gift.getId() : "Enter lore here");
        paper.setItemMeta(meta);
        anvil.setItem(0, paper);
        player.openInventory(anvil);
    }
}