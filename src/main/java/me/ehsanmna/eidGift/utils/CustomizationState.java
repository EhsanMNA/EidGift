package me.ehsanmna.eidGift.utils;

import me.ehsanmna.eidGift.items.GiftItem;

public class CustomizationState {
    public GiftItem gift;
    public String targetName;
    public String customName;
    public String customLore;

    public CustomizationState(GiftItem gift, String targetName) {
        this.gift = gift;
        this.targetName = targetName;
    }
}