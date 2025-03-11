package me.ehsanmna.eidGift.listeners;

import me.ehsanmna.eidGift.EidGift;
import me.ehsanmna.eidGift.commands.ClaimKadoCommand;
import me.ehsanmna.eidGift.utils.ClaimManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClaimListener implements Listener {
    private final ClaimKadoCommand claimCommand;

    public ClaimListener(EidGift plugin) {
        this.claimCommand = new ClaimKadoCommand(plugin, plugin.getGiftManager(), new ClaimManager(plugin), plugin.getMessageManager());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        claimCommand.handleClaimClick(event);
    }
}