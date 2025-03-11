package me.ehsanmna.eidGift.config;

import me.ehsanmna.eidGift.EidGift;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

public class ConfigManager {
    private final EidGift plugin;
    private FileConfiguration config, data, particles, messages;

    public ConfigManager(EidGift plugin) {
        this.plugin = plugin;
    }

    public void setupConfigs() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();

        File dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) plugin.saveResource("data.yml", false);
        data = YamlConfiguration.loadConfiguration(dataFile);

        File particleFile = new File(plugin.getDataFolder(), "particles.yml");
        if (!particleFile.exists()) plugin.saveResource("particles.yml", false);
        particles = YamlConfiguration.loadConfiguration(particleFile);

        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) plugin.saveResource("messages.yml", false);
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public FileConfiguration getConfig() { return config; }
    public FileConfiguration getData() { return data; }
    public FileConfiguration getParticles() { return particles; }
    public FileConfiguration getMessages() { return messages; }
    public void saveData() {
        try {
            data.save(new File(plugin.getDataFolder(), "data.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}