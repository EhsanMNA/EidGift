package me.ehsanmna.eidGift;

import org.bukkit.plugin.java.JavaPlugin;

public class EidGift extends JavaPlugin {
    private GiftManager giftManager;

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist
        saveDefaultConfig();

        // Initialize gift manager and load items
        giftManager = new GiftManager(this);
        giftManager.loadGifts();

        // Register command
        GiveKadoCommand command = new GiveKadoCommand(giftManager);
        getCommand("givekado").setExecutor(command);
        getCommand("givekado").setTabCompleter(command);

        getLogger().info("EidGift plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("EidGift plugin disabled!");
    }
}
