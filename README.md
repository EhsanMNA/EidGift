# EidGift - A Minecraft Spigot Plugin

EidGift is a Spigot plugin that allows server administrators to create custom gift items for players, perfect for celebrating Eid or any special occasion. The plugin supports configurable items with various properties including custom model data, enchantments, item flags, and player skull textures.

## Features
- Define custom gift items in config.yml
- Support for:
  - Custom display names with player name placeholders
  - Lore with placeholders
  - Custom model data
  - Enchantments
  - Item flags
  - Player skull textures
- Items are loaded into memory on startup (no runtime config access)
- Simple command system to give gifts to players

## Installation
1. Download the latest EidGift.jar from the releases page
2. Place the jar file in your Spigot server's `plugins` folder
3. Restart your server
4. Configure the gifts in `plugins/EidGift/config.yml`
5. Reload or restart the server to apply changes

## Commands
- `/givekado <playername> <giftname>` - Gives the specified gift to a player
  - Example: `/givekado Steve item1`
- `/givekado reload` - Reloads the plugin configuration from config.yml
  - Example: `/givekado reload`

## Configuration
Default config.yml:
```yaml
item1:
  name: "Gift to %name%"
  material: STONE
  custommodeldata: 2
  lore:
    - "Eid mobarak %name%"
  enchantments:
    durability: 3
  itemflags:
    - HIDE_ENCHANTS
    - HIDE_ATTRIBUTES

skullitem:
  name: "Special %name% Head"
  material: PLAYER_HEAD
  skulltexture: "texture_url_here"
```

### Config Options
- `name`: Display name of the item (supports `%name%` placeholder)
- `material`: Minecraft material type (e.g., STONE, PLAYER_HEAD)
- `custommodeldata`: Custom model data number
- `lore`: List of lore lines (supports `%name%` placeholder)
- `enchantments`: Key-value pairs of enchantment names and levels
- `itemflags`: List of item flags to apply
- `skulltexture`: Base64 texture value for player heads (only for PLAYER_HEAD material)

## Permissions
- `eidgift.givekado`
  - Description: Allows use of the /givekado command to give gifts
  - Default: op (only operators have access by default)
- `eidgift.reload`
  - Description: Allows use of the /givekado reload command
  - Default: op (only operators have access by default)

To grant permissions using a plugin like LuckPerms:
```bash
/lp user <playername> permission set eidgift.givekado true
/lp user <playername> permission set eidgift.reload true
```
## Requirements
- Spigot/Paper 1.13 or higher
- Java 8 or higher

## Building from Source
1. Clone the repository:
```bash
git clone https://github.com/ehsanmna/EidGift.git
```
2. Build with Maven:
```bash
cd EidGift
mvn clean package
```
3. Find the compiled jar in the `target` folder

## Contributing
Feel free to submit pull requests or open issues for:
- Bug reports
- Feature requests
- Performance improvements

## License
[MIT License](LICENSE)

## Support
For support, please:
1. Check the existing issues
2. Open a new issue on GitHub
3. Provide detailed information about your problem

Happy Eid and enjoy gifting!