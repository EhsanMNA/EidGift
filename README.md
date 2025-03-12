
---

# EidGift - A Minecraft Spigot Plugin

EidGift is a Spigot plugin designed to enhance celebrations like Eid or any special occasion on your Minecraft server. It allows server administrators to define custom gift items and provides a festive `/eidi` command that delivers console-executed commands, fireworks, and messages—limited to one use per player.

## Features
- **Custom Gift Items**:
  - Define gifts in `config.yml` with properties like:
    - Custom display names and lore (with `%name%` and `%date%` placeholders)
    - Materials, custom model data, enchantments, and item flags
    - Player skull textures (for `PLAYER_HEAD`)
  - Loaded into memory on startup for performance
- **/givekado Command**:
  - Give predefined gifts to players with tab completion
- **/eidi Command**:
  - Executes configurable console commands
  - Spawns a celebratory firework at the player’s location
  - Sends customizable messages to the player
  - Restricted to one use per player, tracked in `eidi-data.yml`
- **Reloadable Configuration**: Update gifts without restarting the server

## Installation
1. Download the latest `EidGift.jar` from the [releases page](#).
2. Place the jar in your Spigot server’s `plugins` folder.
3. Restart your server to generate default files (`config.yml` and `eidi-data.yml`).
4. Configure gifts in `plugins/EidGift/config.yml` and check `eidi-data.yml` for usage tracking.
5. Use `/givekado reload` or restart the server to apply changes.

## Commands
- **/givekado <playername> <giftname>**
  - Gives a specified gift to an online player.
  - Example: `/givekado Steve item1`
- **/givekado reload**
  - Reloads the plugin configuration from `config.yml`.
  - Example: `/givekado reload`
- **/eidi**
  - Executes console commands, spawns a firework, and sends messages to the player.
  - Can only be used once per player (tracked via UUID).
  - Example: `/eidi`

## Configuration
### config.yml
Default `config.yml` example:
```yaml
# Gift definitions
item1:
  name: "Gift to %name%"
  material: STONE
  custommodeldata: 2
  lore:
    - "Eid Mobarak %name%"
    - "Given on %date%"
  enchantments:
    durability: 3
  itemflags:
    - HIDE_ENCHANTS
    - HIDE_ATTRIBUTES

skullitem:
  name: "Special %name% Head"
  material: PLAYER_HEAD
  skulltexture: "texture_url_here"

# Eidi command configuration
eidi_commands:
  - "tell %player% Happy Eid!"
  - "give %player% diamond 1"
eidi_messages:
  - "§aHappy Eid, %player%!"
  - "§6Enjoy your special gift!"
  - "§eA firework has been launched for you!"
```
#### Config Options
- **Gift Properties**:
  - `name`: Item display name (`%name%` for player name, `%date%` for Jalali date)
  - `material`: Minecraft material (e.g., `STONE`, `PLAYER_HEAD`)
  - `custommodeldata`: Custom model data number
  - `lore`: List of lore lines
  - `enchantments`: Enchantment names and levels (e.g., `durability: 3`)
  - `itemflags`: List of flags (e.g., `HIDE_ENCHANTS`)
  - `skulltexture`: Base64 texture for `PLAYER_HEAD`
- **Eidi Settings**:
  - `eidi_commands`: List of console commands (`%player%` placeholder)
  - `eidi_messages`: List of messages sent to the player (`%player%` placeholder)

### eidi-data.yml
Tracks players who have used `/eidi`. Example after use:
```yaml
received-players:
  - "123e4567-e89b-12d3-a456-426614174000"
```
- **received-players**: List of player UUIDs who have received an eidi.

## Permissions
- **`eidgift.givekado`**
  - Allows use of `/givekado <player> <gift>`.
  - Default: `op`
- **`eidgift.reload`**
  - Allows use of `/givekado reload`.
  - Default: `op`
- **`eidgift.eidi`**
  - Allows use of `/eidi`.
  - Default: `true` (all players can use it unless restricted)

Grant permissions with a plugin like LuckPerms:
```bash
/lp user <playername> permission set eidgift.givekado true
/lp user <playername> permission set eidgift.reload true
/lp user <playername> permission set eidgift.eidi true
```

## Requirements
- Spigot/Paper 1.13 or higher
- Java 8 or higher
- (Optional) Permissions plugin for fine-tuned access control

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
3. Find the compiled jar in the `target` folder.

## Contributing
Contributions are welcome! Submit pull requests or open issues for:
- Bug reports
- Feature requests (e.g., configurable fireworks, cooldowns)
- Performance improvements

## License
[MIT License](LICENSE)

## Created with AI Assistance
This project was developed with the assistance of AI.

## Support
For help:
1. Check existing [GitHub issues](#).
2. Open a new issue with:
  - Plugin version
  - Server version
  - Detailed description of the problem

Happy Eid! Enjoy gifting and celebrating with EidGift!

---

### Changes Made
- **New Section**: Added "Created with AI Assistance" to give credit to Grok and xAI for their role in the development.
- **Placement**: Positioned it before "Support" to keep it prominent but not disrupt the main instructional content.
- **Tone**: Kept it concise and professional, emphasizing collaboration.

Let me know if you’d like to tweak the wording or placement further!