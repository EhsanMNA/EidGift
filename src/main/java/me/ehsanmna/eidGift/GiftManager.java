package me.ehsanmna.eidGift;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GiftManager {
    private final EidGift plugin;
    private Map<String, GiftItem> gifts = new HashMap<>();

    public GiftManager(EidGift plugin) {
        this.plugin = plugin;
    }

    public void loadGifts() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("");
        if (config == null) return;

        gifts.clear(); // Clear existing gifts before loading
        for (String key : config.getKeys(false)) {
            ConfigurationSection itemSection = config.getConfigurationSection(key);

            GiftItem gift = new GiftItem(
                    key,
                    itemSection.getString("name"),
                    Material.valueOf(itemSection.getString("material").toUpperCase())
            );

            gift.setCustomModelData(itemSection.getInt("custommodeldata", 0));
            gift.setLore(itemSection.getStringList("lore"));

            ConfigurationSection enchants = itemSection.getConfigurationSection("enchantments");
            if (enchants != null) {
                Map<Enchantment, Integer> enchantMap = new HashMap<>();
                for (String ench : enchants.getKeys(false)) {
                    enchantMap.put(Enchantment.getByName(ench.toUpperCase()),
                            enchants.getInt(ench));
                }
                gift.setEnchantments(enchantMap);
            }

            if (itemSection.contains("itemflags")) {
                List<ItemFlag> flags = itemSection.getStringList("itemflags").stream()
                        .map(flag -> ItemFlag.valueOf(flag.toUpperCase()))
                        .collect(Collectors.toList());
                gift.setItemFlags(flags);
            }

            gift.setSkullTexture(itemSection.getString("skulltexture"));

            gifts.put(key.toLowerCase(), gift);
        }
    }

    public void reloadGifts() {
        plugin.reloadConfig(); // Reload the config file
        loadGifts(); // Reload gifts from the updated config
    }

    public GiftItem getGift(String id) {
        return gifts.get(id.toLowerCase());
    }

    // New method to get all gift IDs
    public Set<String> getGiftIds() {
        return gifts.keySet();
    }
}