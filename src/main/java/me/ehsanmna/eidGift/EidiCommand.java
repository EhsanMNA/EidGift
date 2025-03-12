package me.ehsanmna.eidGift;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import java.util.List;

public class EidiCommand implements CommandExecutor {
    private final EidGift plugin;

    public EidiCommand(EidGift plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        // Check permission
        if (!player.hasPermission("eidgift.eidi")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        // Check if player has already received eidi
        EidiDataManager dataManager = plugin.getEidiDataManager();
        if (dataManager.hasReceivedEidi(player.getUniqueId())) {
            player.sendMessage("§cYou have already received your eidi!");
            return true;
        }

        // Execute console commands
        List<String> commands = plugin.getConfig().getStringList("eidi_commands");
        if (!commands.isEmpty()) {
            for (String cmd : commands) {
                String formattedCmd = cmd.replace("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCmd);
            }
        }

        // Spawn a firework at the player's location
        spawnFirework(player);

        // Send custom messages from config
        List<String> messages = plugin.getConfig().getStringList("eidi_messages");
        if (messages.isEmpty()) {
            player.sendMessage("§aEidi commands executed, but no messages are configured!");
        } else {
            for (String message : messages) {
                String formattedMessage = message.replace("%player%", player.getName());
                player.sendMessage(formattedMessage);
            }
        }

        // Mark player as having received eidi
        dataManager.addReceivedPlayer(player.getUniqueId());

        return true;
    }

    private void spawnFirework(Player player) {
        Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();

        FireworkEffect effect = FireworkEffect.builder()
                .withColor(Color.GREEN)
                .withFade(Color.YELLOW)
                .withFade(Color.RED)
                .withFade(Color.BLUE)
                .withFade(Color.BLACK)
                .with(FireworkEffect.Type.BURST)
                .trail(true)
                .flicker(true)
                .build();

        meta.addEffect(effect);
        meta.setPower(1);
        firework.setFireworkMeta(meta);
    }
}