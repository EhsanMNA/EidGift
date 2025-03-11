package me.ehsanmna.eidGift.api;

import me.ehsanmna.eidGift.EidGift;
import me.ehsanmna.eidGift.items.GiftItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EidGiftAPI {
    private final EidGift plugin;

    public EidGiftAPI(EidGift plugin) {
        this.plugin = plugin;
    }

    public void giveGift(Player sender, Player target, String giftId) {
        GiftItem gift = plugin.getGiftManager().getGift(giftId);
        if (gift != null) {
            Bukkit.dispatchCommand(sender, "givekado " + target.getName() + " " + giftId); // Use dispatchCommand
        }
    }

    public void queueGift(String playerName, String giftId) {
        plugin.getClaimManager().addClaim(playerName, giftId);
    }

    public GiftItem getGift(String giftId) {
        return plugin.getGiftManager().getGift(giftId);
    }

    public List<String> getGiftIds() {
        return new ArrayList<>(plugin.getGiftManager().getGiftIds());
    }

    public boolean hasQueuedGifts(String playerName) {
        return !plugin.getClaimManager().getClaims(playerName).isEmpty();
    }

    public List<String> getQueuedGifts(String playerName) {
        return plugin.getClaimManager().getClaims(playerName);
    }

    public ItemStack createGiftItem(String giftId, String playerName, String customName, String customLore) {
        GiftItem gift = plugin.getGiftManager().getGift(giftId);
        if (gift == null) return null;
        if (customName != null) gift.setCustomization("name", customName);
        if (customLore != null) gift.setCustomization("lore", customLore);
        return gift.createItem(playerName);
    }

    public void registerGift(String giftId, GiftItem gift) {
        plugin.getGiftManager().getGifts().put(giftId.toLowerCase(), gift);
    }
}