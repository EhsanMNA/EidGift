package me.ehsanmna.eidGift.items;

import me.ehsanmna.eidGift.EidGift;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class GiftManager {
    private final EidGift plugin;
    private Map<String, GiftItem> gifts = new HashMap<>();
    private Map<String, GiftItem> wrappers = new HashMap<>();

    public GiftManager(EidGift plugin) {
        this.plugin = plugin;
    }

    public void loadGifts() {
        gifts.clear();
        wrappers.clear();

        // Load wrappers
        ConfigurationSection wrappersSection = plugin.getConfigManager().getConfig().getConfigurationSection("wrappers");
        if (wrappersSection != null) {
            for (String key : wrappersSection.getKeys(false)) {
                ConfigurationSection wrapper = wrappersSection.getConfigurationSection(key);
                GiftItem gift = new GiftItem(key, wrapper.getString("name"), Material.valueOf(wrapper.getString("material").toUpperCase()));
                gift.setLore(wrapper.getStringList("lore"));
                wrappers.put(key.toLowerCase(), gift);
            }
        }

        // Load gifts
        ConfigurationSection giftsSection = plugin.getConfigManager().getConfig().getConfigurationSection("gifts");
        if (giftsSection != null) {
            for (String key : giftsSection.getKeys(false)) {
                ConfigurationSection itemSection = giftsSection.getConfigurationSection(key);
                GiftItem gift = new GiftItem(key, itemSection.getString("name"), Material.valueOf(itemSection.getString("material").toUpperCase()));
                gift.setCustomModelData(itemSection.getInt("custommodeldata", 0));
                gift.setLore(itemSection.getStringList("lore"));
                gift.setWrapped(itemSection.getBoolean("wrapped", false));
                gift.setWrappingItem(itemSection.getString("wrapping-item"));
                gift.setCategory(itemSection.getString("category"));
                gift.setSound(itemSection.getString("sound"));
                gift.setParticle(itemSection.getString("particle"));
                gift.setCost(itemSection.getDouble("cost", 0.0));
                gift.setExpirationHours(itemSection.getInt("expiration", 0));
                gift.setRarity(itemSection.getString("rarity"));
                gift.setWrapParticle(itemSection.getString("wrap-animation.particle"));
                gift.setUnwrapParticle(itemSection.getString("unwrap-animation.particle"));
                gift.setWrapSound(itemSection.getString("wrap-animation.sound"));
                gift.setUnwrapSound(itemSection.getString("unwrap-animation.sound"));

                if (itemSection.contains("active-dates")) {
                    ConfigurationSection dates = itemSection.getConfigurationSection("active-dates");
                    if (dates.contains("start"))
                        gift.setStartDate(LocalDateTime.parse(dates.getString("start"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    if (dates.contains("end"))
                        gift.setEndDate(LocalDateTime.parse(dates.getString("end"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                }

                if (itemSection.contains("loot")) {
                    List<LootItem> lootTable = new ArrayList<>();
                    for (String loot : itemSection.getStringList("loot")) {
                        String[] parts = loot.split(",");
                        lootTable.add(new LootItem(Material.valueOf(parts[0].trim().toUpperCase()), Double.parseDouble(parts[1].trim())));
                    }
                    gift.setLootTable(lootTable);
                }

                if (itemSection.contains("bundle")) {
                    gift.setBundleItems(itemSection.getStringList("bundle"));
                }

                ConfigurationSection enchants = itemSection.getConfigurationSection("enchantments");
                if (enchants != null) {
                    Map<Enchantment, Integer> enchantMap = new HashMap<>();
                    for (String ench : enchants.getKeys(false)) {
                        enchantMap.put(Enchantment.getByName(ench.toUpperCase()), enchants.getInt(ench));
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
    }

    public void registerRecipes() {
        ConfigurationSection recipesSection = plugin.getConfigManager().getConfig().getConfigurationSection("recipes");
        if (recipesSection == null) return;

        for (String key : recipesSection.getKeys(false)) {
            ConfigurationSection recipeSection = recipesSection.getConfigurationSection(key);
            String resultId = recipeSection.getString("result");
            GiftItem resultGift = getGift(resultId);
            if (resultGift == null) continue;

            NamespacedKey recipeKey = new NamespacedKey(plugin, key);
            ShapedRecipe recipe = new ShapedRecipe(recipeKey, resultGift.createItem("Crafter"));
            recipe.shape(recipeSection.getStringList("shape").toArray(new String[0]));

            ConfigurationSection ingredients = recipeSection.getConfigurationSection("ingredients");
            for (String ingredientKey : ingredients.getKeys(false)) {
                recipe.setIngredient(ingredientKey.charAt(0), Material.valueOf(ingredients.getString(ingredientKey).toUpperCase()));
            }

            Bukkit.addRecipe(recipe);
        }
    }

    public void reloadGifts() {
        plugin.getConfigManager().setupConfigs();
        loadGifts();
        registerRecipes();
    }

    public GiftItem getGift(String id) { return gifts.get(id.toLowerCase()); }
    public GiftItem getWrapper(String id) { return wrappers.get(id.toLowerCase()); }
    public Set<String> getGiftIds() { return gifts.keySet(); }
    public Map<String, GiftItem> getGifts() { return gifts; }

    public EidGift getPlugin() {
        return plugin;
    }
}