package me.ehsanmna.eidGift.listeners;

import me.ehsanmna.eidGift.api.events.GiftUnwrapEvent;
import me.ehsanmna.eidGift.config.MessageManager;
import me.ehsanmna.eidGift.config.ParticleConfig;
import me.ehsanmna.eidGift.items.GiftItem;
import me.ehsanmna.eidGift.items.GiftManager;
import me.ehsanmna.eidGift.utils.ClaimManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class GiftInteractListener implements Listener {
    private final GiftManager giftManager;
    private final ClaimManager claimManager;
    private final MessageManager messageManager;

    public GiftInteractListener(GiftManager giftManager, ClaimManager claimManager, MessageManager messageManager) {
        this.giftManager = giftManager;
        this.claimManager = claimManager;
        this.messageManager = messageManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        for (GiftItem gift : giftManager.getGifts().values()) {
            if (gift.isWrapped() && giftManager.getWrapper(gift.getWrappingItem()).createItem(player.getName()).isSimilar(item)) {
                event.setCancelled(true);
                item.setAmount(item.getAmount() - 1);

                GiftUnwrapEvent unwrapEvent = new GiftUnwrapEvent(player, gift);
                Bukkit.getPluginManager().callEvent(unwrapEvent);
                if (unwrapEvent.isCancelled()) return;

                if (gift.getLootTable() != null) {
                    ItemStack loot = getRandomLoot(gift);
                    giveItem(player, loot, gift);
                } else if (gift.getBundleItems() != null) {
                    for (String bundleId : gift.getBundleItems()) {
                        GiftItem bundledGift = giftManager.getGift(bundleId);
                        if (bundledGift != null) giveItem(player, bundledGift.createItem(player.getName()), gift);
                    }
                } else {
                    giveItem(player, gift.createItem(player.getName()), gift);
                }

                playUnwrapAnimation(player, gift);
                return;
            }
        }
    }

    private void giveItem(Player player, ItemStack item, GiftItem gift) {
        if (player.getInventory().firstEmpty() == -1) {
            claimManager.addClaim(player.getName(), gift.getId());
            player.sendMessage(messageManager.getMessage("inventory-full-claim"));
        } else {
            player.getInventory().addItem(item);
            player.sendMessage(messageManager.getMessage("gift-unwrapped"));
        }
    }

    private ItemStack getRandomLoot(GiftItem gift) {
        Random random = new Random();
        double roll = random.nextDouble();
        double cumulative = 0.0;
        for (me.ehsanmna.eidGift.items.LootItem loot : gift.getLootTable()) {
            cumulative += loot.getChance();
            if (roll <= cumulative) {
                return new ItemStack(loot.getMaterial());
            }
        }
        return gift.createItem(""); // Fallback
    }

    private void playUnwrapAnimation(Player player, GiftItem gift) {
        if (gift.getUnwrapSound() != null) {
            player.playSound(player.getLocation(), Sound.valueOf(gift.getUnwrapSound()), 1.0f, 1.0f);
        }
        if (gift.getUnwrapParticle() != null) {
            ParticleConfig particleConfig = new ParticleConfig(giftManager.getPlugin().getConfigManager().getParticles().getConfigurationSection(gift.getUnwrapParticle()));
            player.spawnParticle(particleConfig.getType(), player.getLocation(), particleConfig.getCount());
        }
    }
}