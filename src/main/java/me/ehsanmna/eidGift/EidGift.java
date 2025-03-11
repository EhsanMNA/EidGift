package me.ehsanmna.eidGift;

import me.ehsanmna.eidGift.commands.ClaimKadoCommand;
import me.ehsanmna.eidGift.commands.GiveKadoCommand;
import me.ehsanmna.eidGift.config.ConfigManager;
import me.ehsanmna.eidGift.config.MessageManager;
import me.ehsanmna.eidGift.items.GiftManager;
import me.ehsanmna.eidGift.listeners.ClaimListener;
import me.ehsanmna.eidGift.listeners.GiftCustomizeListener;
import me.ehsanmna.eidGift.listeners.GiftInteractListener;
import me.ehsanmna.eidGift.utils.ClaimManager;
import me.ehsanmna.eidGift.utils.EconomyManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EidGift extends JavaPlugin {
    private GiftManager giftManager;
    private ConfigManager configManager;
    private MessageManager messageManager;
    private ClaimManager claimManager;
    private EconomyManager economyManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.setupConfigs();

        messageManager = new MessageManager(this);

        giftManager = new GiftManager(this);
        giftManager.loadGifts();
        giftManager.registerRecipes();

        claimManager = new ClaimManager(this);
        economyManager = new EconomyManager(this);

        GiveKadoCommand giveCommand = new GiveKadoCommand(this, giftManager, claimManager, economyManager, messageManager);
        getCommand("givekado").setExecutor(giveCommand);
        getCommand("givekado").setTabCompleter(giveCommand);

        ClaimKadoCommand claimCommand = new ClaimKadoCommand(this, giftManager, claimManager, messageManager);
        getCommand("claimkado").setExecutor(claimCommand);

        getServer().getPluginManager().registerEvents(new GiftInteractListener(giftManager, claimManager, messageManager), this);
        getServer().getPluginManager().registerEvents(new ClaimListener(this), this);
        getServer().getPluginManager().registerEvents(new GiftCustomizeListener(this), this);

        getLogger().info("EidGift plugin enabled!");
    }

    @Override
    public void onDisable() {
        claimManager.saveClaims();
        getLogger().info("EidGift plugin disabled!");
    }

    public ConfigManager getConfigManager() { return configManager; }
    public MessageManager getMessageManager() { return messageManager; }
    public GiftManager getGiftManager() { return giftManager; }
    public ClaimManager getClaimManager() { return claimManager; } // Added
}