package me.ehsanmna.eidGift.items;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class GiftItem {
    private String id, name, category, sound, particle, skullTexture, wrapParticle, unwrapParticle, wrapSound, unwrapSound;
    private Material material;
    private Integer customModelData;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private List<ItemFlag> itemFlags;
    private boolean wrapped;
    private String wrappingItem;
    private double cost;
    private LocalDateTime startDate, endDate;
    private int expirationHours;
    private String rarity;
    private List<LootItem> lootTable;
    private List<String> bundleItems;
    private Map<String, String> customizations;

    public GiftItem(String id, String name, Material material) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.customizations = new HashMap<>();
    }

    // Getters and setters
    public void setCustomModelData(Integer customModelData) { this.customModelData = customModelData; }
    public void setLore(List<String> lore) { this.lore = lore; }
    public void setEnchantments(Map<Enchantment, Integer> enchantments) { this.enchantments = enchantments; }
    public void setItemFlags(List<ItemFlag> itemFlags) { this.itemFlags = itemFlags; }
    public void setSkullTexture(String skullTexture) { this.skullTexture = skullTexture; }
    public void setWrapped(boolean wrapped) { this.wrapped = wrapped; }
    public void setWrappingItem(String wrappingItem) { this.wrappingItem = wrappingItem; }
    public void setCategory(String category) { this.category = category; }
    public void setSound(String sound) { this.sound = sound; }
    public void setParticle(String particle) { this.particle = particle; }
    public void setCost(double cost) { this.cost = cost; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public void setExpirationHours(int expirationHours) { this.expirationHours = expirationHours; }
    public void setRarity(String rarity) { this.rarity = rarity; }
    public void setLootTable(List<LootItem> lootTable) { this.lootTable = lootTable; }
    public void setBundleItems(List<String> bundleItems) { this.bundleItems = bundleItems; }
    public void setWrapParticle(String wrapParticle) { this.wrapParticle = wrapParticle; }
    public void setUnwrapParticle(String unwrapParticle) { this.unwrapParticle = unwrapParticle; }
    public void setWrapSound(String wrapSound) { this.wrapSound = wrapSound; }
    public void setUnwrapSound(String unwrapSound) { this.unwrapSound = unwrapSound; }
    public String getId() { return id; }
    public String getWrappingItem() { return wrappingItem; }
    public String getCategory() { return category; }
    public String getSound() { return sound; }
    public String getParticle() { return particle; }
    public double getCost() { return cost; }
    public String getRarity() { return rarity; }
    public List<LootItem> getLootTable() { return lootTable; }
    public List<String> getBundleItems() { return bundleItems; }
    public String getWrapParticle() { return wrapParticle; }
    public String getUnwrapParticle() { return unwrapParticle; }
    public String getWrapSound() { return wrapSound; }
    public String getUnwrapSound() { return unwrapSound; }
    public void setCustomization(String key, String value) { customizations.put(key, value); }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return (startDate == null || !now.isBefore(startDate)) && (endDate == null || !now.isAfter(endDate));
    }

    public ItemStack createItem(String playerName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        String displayName = customizations.getOrDefault("name", name);
        meta.setDisplayName(displayName.replace("%name%", playerName).replace("%category%", category != null ? category : ""));
        if (lore != null || customizations.containsKey("lore")) {
            List<String> finalLore = customizations.containsKey("lore") ? List.of(customizations.get("lore").split("\n")) : lore;
            meta.setLore(finalLore.stream()
                    .map(line -> line.replace("%name%", playerName).replace("%category%", category != null ? category : ""))
                    .collect(java.util.stream.Collectors.toList()));
        }
        if (customModelData != null) meta.setCustomModelData(customModelData);
        if (enchantments != null) enchantments.forEach((ench, level) -> meta.addEnchant(ench, level, true));
        if (itemFlags != null) itemFlags.forEach(meta::addItemFlags);
        if (material == Material.PLAYER_HEAD && skullTexture != null && meta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) meta;
            applySkullTexture(skullMeta, skullTexture);
        }
        item.setItemMeta(meta);
        return item;
    }

    private void applySkullTexture(SkullMeta skullMeta, String texture) {
        try {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", texture));
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isWrapped() { return wrapped; }
    public int getExpirationHours() { return expirationHours; }
}