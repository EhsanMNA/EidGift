# EidGift API

The EidGift plugin provides a public API for developers to extend its functionality. This document outlines the available methods and events.

## Getting Started
To use the API, depend on EidGift in your plugin:
```yaml
depend: [EidGift]
```
Access the API via:
```java
EidGiftAPI api = new EidGiftAPI(Bukkit.getPluginManager().getPlugin("EidGift"));
```

## API Methods
### `EidGiftAPI`
#### `void giveGift(Player sender, Player target, String giftId)`
- Gives a gift from `sender` to `target`.
- Example: `api.giveGift(player, target, "item1");`

#### `void queueGift(String playerName, String giftId)`
- Queues a gift for a player.
- Example: `api.queueGift("Steve", "item1");`

#### `GiftItem getGift(String giftId)`
- Retrieves a `GiftItem` by ID.
- Example: `GiftItem gift = api.getGift("item1");`

#### `List<String> getGiftIds()`
- Returns all registered gift IDs.
- Example: `List<String> ids = api.getGiftIds();`

#### `boolean hasQueuedGifts(String playerName)`
- Checks if a player has queued gifts.
- Example: `boolean hasGifts = api.hasQueuedGifts("Steve");`

#### `List<String> getQueuedGifts(String playerName)`
- Gets a list of queued gift IDs for a player.
- Example: `List<String> gifts = api.getQueuedGifts("Steve");`

#### `ItemStack createGiftItem(String giftId, String playerName, String customName, String customLore)`
- Creates a gift item with custom name and lore.
- Example: `ItemStack item = api.createGiftItem("item1", "Steve", "Custom Gift", "Special lore");`

#### `void registerGift(String giftId, GiftItem gift)`
- Registers a custom gift.
- Example:
  ```java
  GiftItem customGift = new GiftItem("custom1", "Custom Gift", Material.DIAMOND);
  api.registerGift("custom1", customGift);
  ```

## Events
### `GiftGiveEvent`
- Triggered when a gift is given.
- Fields: `Player getSender()`, `Player getTarget()`, `GiftItem getGift()`
- Cancellable: Yes

### `GiftClaimEvent`
- Triggered when a gift is claimed.
- Fields: `Player getPlayer()`, `GiftItem getGift()`
- Cancellable: Yes

### `GiftUnwrapEvent`
- Triggered when a wrapped gift is unwrapped.
- Fields: `Player getPlayer()`, `GiftItem getGift()`
- Cancellable: Yes

## Example Plugin
```java
import me.ehsanmna.eidGift.api.EidGiftAPI;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MyPlugin extends JavaPlugin implements Listener {
    private EidGiftAPI api;

    @Override
    public void onEnable() {
        api = new EidGiftAPI(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onGiftGive(GiftGiveEvent event) {
        event.getSender().sendMessage("You gave a gift!");
    }
}
```
