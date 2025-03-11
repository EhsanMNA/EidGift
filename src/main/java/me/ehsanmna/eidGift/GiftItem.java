package me.ehsanmna.eidGift;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GiftItem {
    private String id;
    private String name;
    private Material material;
    private Integer customModelData;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private List<ItemFlag> itemFlags;
    private String skullTexture;

    public GiftItem(String id, String name, Material material) {
        this.id = id;
        this.name = name;
        this.material = material;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Integer getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(Integer customModelData) {
        this.customModelData = customModelData;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public void setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    public List<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    public void setItemFlags(List<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
    }

    public String getSkullTexture() {
        return skullTexture;
    }

    public void setSkullTexture(String skullTexture) {
        this.skullTexture = skullTexture;
    }

    public ItemStack createItem(String playerName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        // Set name with player placeholder
        meta.setDisplayName(name.replace("%name%", playerName));

        // Set lore with player placeholder
        if (lore != null) {
            List<String> formattedLore = lore.stream()
                    .map(line -> line.replace("%name%", playerName))
                    .collect(Collectors.toList());
            meta.setLore(formattedLore);
        }

        // Set custom model data
        if (customModelData != null) {
            meta.setCustomModelData(customModelData);
        }

        // Add enchantments
        if (enchantments != null) {
            enchantments.forEach((ench, level) -> meta.addEnchant(ench, level, true));
        }

        // Add item flags
        if (itemFlags != null) {
            itemFlags.forEach(meta::addItemFlags);
        }

        // Handle skull texture (only for PLAYER_HEAD)
        if (material == Material.PLAYER_HEAD && skullTexture != null && meta instanceof SkullMeta skullMeta) {
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
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
