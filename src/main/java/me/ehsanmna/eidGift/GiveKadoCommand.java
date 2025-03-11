package me.ehsanmna.eidGift;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveKadoCommand implements CommandExecutor {
    private final GiftManager giftManager;

    public GiveKadoCommand(GiftManager giftManager) {
        this.giftManager = giftManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("Usage: /givekado <playername> <giftname>");
            return true;
        }

        String targetName = args[0];
        String giftId = args[1];

        GiftItem gift = giftManager.getGift(giftId);
        if (gift == null) {
            sender.sendMessage("Gift '" + giftId + "' not found!");
            return true;
        }

        player.getInventory().addItem(gift.createItem(targetName));
        sender.sendMessage("Gave " + giftId + " to " + targetName + "!");
        return true;
    }
}