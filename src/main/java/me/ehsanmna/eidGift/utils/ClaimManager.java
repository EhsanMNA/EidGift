package me.ehsanmna.eidGift.utils;

import me.ehsanmna.eidGift.EidGift;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClaimManager {
    private final EidGift plugin;

    public ClaimManager(EidGift plugin) {
        this.plugin = plugin;
    }

    public void addClaim(String playerName, String giftId) {
        List<String> playerClaims = getClaims(playerName);
        playerClaims.add(giftId + ";" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        plugin.getConfigManager().getData().set("claims." + playerName, playerClaims);
        saveClaims();
    }

    public List<String> getClaims(String playerName) {
        return plugin.getConfigManager().getData().getStringList("claims." + playerName);
    }

    public void saveClaims() {
        plugin.getConfigManager().saveData();
    }
}