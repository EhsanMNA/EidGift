# EidGift Plugin

EidGift is a Minecraft plugin that adds a gift-giving system to your server, perfect for celebrating events like Eid or other special occasions. Players can give, claim, craft, and customize gifts with various features like rarity, expiration, and animations.

## Features
- **Gift Giving**: Use `/givekado` to send gifts to players.
- **Gift Claiming**: Use `/claimkado` to claim queued gifts via a GUI.
- **Crafting System**: Craft gifts using custom recipes.
- **Rarity & Loot Tables**: Gifts can have random loot with defined probabilities.
- **Expiration**: Gifts expire after a set time if not claimed.
- **Customization**: Customize gift names and lore via a GUI with anvil input.
- **Animations**: Particle and sound effects for wrapping/unwrapping gifts.
- **Bundles**: Combine multiple gifts into one.
- **Notifications**: Notify players of new gifts via title, chat, or action bar.
- **API**: Extend functionality with a robust API.

## Installation
1. Download the latest `EidGift.jar` from the releases page.
2. Place it in your server's `plugins` folder.
3. Restart the server.
4. Configure `config.yml`, `messages.yml`, `particles.yml`, and `data.yml` as needed.

## Commands
- `/givekado <player> <gift> [customize]` - Give a gift or open the customization GUI.
- `/givekado reload` - Reload the plugin configuration (requires `eidgift.reload`).
- `/claimkado` - Open the gift claim GUI.

## Permissions
- `eidgift.givekado` - Use `/givekado` (default: op).
- `eidgift.reload` - Reload the plugin (default: op).
- `eidgift.claim` - Claim gifts (default: true).

## Configuration
See `config.yml` for gift definitions, recipes, and notifications. Example:
```yaml
gifts:
  item1:
    name: "Gift to %name%"
    material: STONE
    wrapped: true
    wrapping-item: wrappedA
    particle: myCustomParticle
particles:
  myCustomParticle:
    type: VILLAGER_HAPPY
    count: 10
```
## Dependencies
- Vault (optional): For economy integration.
## Development
- API: Check [API.md](docs/API.md) for integrating with EidGift programmatically.
- Source: Available in this repository.
## Contributing
- Feel free to submit issues or pull requests on GitHub!