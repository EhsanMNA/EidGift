package me.ehsanmna.eidGift;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GiveKadoCommand implements CommandExecutor, TabCompleter {
    private final GiftManager giftManager;

    public GiveKadoCommand(GiftManager giftManager) {
        this.giftManager = giftManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check base permission
        if (!sender.hasPermission("eidgift.givekado")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        // Handle reload subcommand
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("eidgift.reload")) {
                sender.sendMessage("§cYou don't have permission to reload the plugin!");
                return true;
            }
            giftManager.reloadGifts();
            sender.sendMessage("§aEidGift configuration reloaded!");
            return true;
        }

        // Main give command logic
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("§cUsage: /givekado <playername> <giftname> or /givekado reload");
            return true;
        }

        String targetName = args[0];
        String giftId = args[1].toLowerCase();

        GiftItem gift = giftManager.getGift(giftId);
        if (gift == null) {
            sender.sendMessage("§cGift '" + giftId + "' not found!");
            return true;
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null || !target.isOnline()) {
            sender.sendMessage("§cPlayer '" + targetName + "' is not online!");
            return true;
        }

        target.getInventory().addItem(gift.createItem(targetName));
        sender.sendMessage("§aGave " + giftId + " to " + targetName + "!");
        target.sendMessage("§aYou received a gift from " + player.getName() + "!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (!sender.hasPermission("eidgift.givekado")) {
            return completions;
        }

        if (args.length == 1) {
            // Suggest players or "reload"
            List<String> suggestions = Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName).collect(Collectors.toList());
            if (sender.hasPermission("eidgift.reload")) {
                suggestions.add("reload");
            }
            completions.addAll(suggestions.stream()
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .sorted() // Sort alphabetically
                    .toList());
        } else if (args.length == 2 && !args[0].equalsIgnoreCase("reload")) {
            // Suggest gift names
            completions.addAll(giftManager.getGiftIds().stream()
                    .filter(id -> id.toLowerCase().startsWith(args[1].toLowerCase()))
                    .sorted() // Sort alphabetically
                    .toList());
        }

        return completions;
    }
}