package me.ehsanmna.eidGift.api.events;

import me.ehsanmna.eidGift.items.GiftItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GiftClaimEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final GiftItem gift;
    private boolean cancelled;

    public GiftClaimEvent(Player player, GiftItem gift) {
        this.player = player;
        this.gift = gift;
        this.cancelled = false;
    }

    public Player getPlayer() { return player; }
    public GiftItem getGift() { return gift; }

    @Override
    public boolean isCancelled() { return cancelled; }
    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    @Override
    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}