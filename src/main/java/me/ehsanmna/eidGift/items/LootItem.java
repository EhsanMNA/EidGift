package me.ehsanmna.eidGift.items;

import org.bukkit.Material;

public class LootItem {
    Material material;
    double chance;

    public LootItem(Material material, double chance) {
        this.material = material;
        this.chance = chance;
    }

    public Material getMaterial() {
        return material;
    }

    public double getChance() {
        return chance;
    }
}