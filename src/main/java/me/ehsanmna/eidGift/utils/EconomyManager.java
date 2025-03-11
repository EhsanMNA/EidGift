package me.ehsanmna.eidGift.utils;

import me.ehsanmna.eidGift.EidGift;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {
    private final EidGift plugin;
    private Economy economy;

    public EconomyManager(EidGift plugin) {
        this.plugin = plugin;
        setupEconomy();
    }

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return economy != null;
    }

    public boolean hasEnough(Player player, double amount) {
        return economy != null && economy.has(player, amount);
    }

    public void withdraw(Player player, double amount) {
        if (economy != null) economy.withdrawPlayer(player, amount);
    }
}