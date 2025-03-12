package me.ehsanmna.eidGift;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EidiDataManager {
    private final EidGift plugin;
    private File dataFile;
    private FileConfiguration dataConfig;

    public EidiDataManager(EidGift plugin) {
        this.plugin = plugin;
        setupDataFile();
    }

    private void setupDataFile() {
        dataFile = new File(plugin.getDataFolder(), "eidi-data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            plugin.saveResource("eidi-data.yml", false); // Copy default if exists, or create empty
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void reloadData() {
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void saveData() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save eidi-data.yml: " + e.getMessage());
        }
    }

    public boolean hasReceivedEidi(UUID playerUUID) {
        List<String> receivedPlayers = dataConfig.getStringList("received-players");
        return receivedPlayers.contains(playerUUID.toString());
    }

    public void addReceivedPlayer(UUID playerUUID) {
        List<String> receivedPlayers = dataConfig.getStringList("received-players");
        if (!receivedPlayers.contains(playerUUID.toString())) {
            receivedPlayers.add(playerUUID.toString());
            dataConfig.set("received-players", receivedPlayers);
            saveData();
        }
    }
}