package me.ehsanmna.eidGift.api.events;

import me.ehsanmna.eidGift.items.GiftItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GiftGiveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player sender;
    private final Player target;
    private final GiftItem gift;
    private boolean cancelled;

    public GiftGiveEvent(Player sender, Player target, GiftItem gift) {
        this.sender = sender;
        this.target = target;
        this.gift = gift;
        this.cancelled = false;
    }

    public Player getSender() { return sender; }
    public Player getTarget() { return target; }
    public GiftItem getGift() { return gift; }

    @Override
    public boolean isCancelled() { return cancelled; }
    @Override
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    @Override
    public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}