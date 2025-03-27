# 🎁 EidGift - Celebrate special occasions in your minecraft server!

EidGift is a Spigot plugin crafted to bring festive magic to your Minecraft community during Eid, holidays, or any special occasion! Perfect for server admins who want to surprise players with custom gifts, dazzling fireworks, and heartwarming messages—all wrapped into one delightful experience.

## ✨ Featuers

- **Customized gifts**:
  - You can define your own gifts in `config.yml` with:
    - Custom names/lore (supports `%name%` and `%date%` placeholders)
    - Materials, custom model-data, enchantments, and item flags
    - Skull textures for `PLAYER_HEAD` items
  - Optimized loading into memory on server startup.

- **Commands**:
  - `/givekado`: Distribute gifts to players (tab completion included).
  - `/eidi`: One-time celebratory command per player, including:
    - Console command execution (e.g., give items)
    - Firework displays at the player’s location
    - Customizable messages

- **Reloadable Configs**: Update gifts or settings without restarting the server.

## 📥 Installation

1. Download the latest `EidGift.jar` from the [releases](https://github.com/EhsanMNA/EidGift/releases) page.
2. Place the JAR in your server’s `/plugins` folder.
3. Restart the server to generate `config.yml` and `eidi-data.yml`.
4. Configure gifts in `/plugins/EidGift/config.yml`
5. Use `/givekado reload` to apply changes.

## ⌨️ Commands

| Command | Description | Example |  
|---------|-------------|---------|  
| `/givekado <player> <gift>` | Give a gift to an online player | `/givekado Steve item1` |  
| `/givekado reload` | Reload the configuration | `/givekado reload` |  
| `/eidi` | Trigger festive actions (one-time use) | `/eidi` |  

## ⚙️ Configuration

`config.yml`:
```yml
# Gift Definitions
item1:
  name: "Gift for %name%"  # %name% = player name, %date% = current date
  material: STONE
  custommodeldata: 2
  lore:
    - "Eid Mubarak, %name%!"
    - "Celebrated on %date%"
  enchantments:
    durability: 3
  itemflags:
    - HIDE_ENCHANTS

skullitem:
  name: "Special %name% Head"
  material: PLAYER_HEAD
  skulltexture: "Texture_URL"  # Required for custom skulls

# /eidi Settings
eidi_commands:
  - "give %player% diamond 1"  # %player% = target's username
eidi_messages:
  - "§aHappy Eid, %player%!"
```

`eidi-data.yml`:
```yml
# Tracks players who used /eidi
received-players:
  - "123e4567-e89b-12d3-a456-426614174000"  # Player UUIDs
```

## 🔒 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `eidgift.givekado` | Use `/givekado <player> <gift>` | `op` |
| `eidgift.reload` | Reload configurations | `op` |
| `eidgift.eidi` | Use `/eidi` | `true` (all players) |

Grant via LuckPerms:
```
/lp user <player> permission set eidgift.givekado
```

## 📋 Requirements

- **Server**: Spigot/Paper 1.13+
- **Java**: JDK 8 or newer
- **Optional**: Permissions plugin (e.g., LuckPerms)

## 🛠️ Building from Source
1. Clone the repo:
```
git clone https://github.com/ehsanmna/EidGift.git
```

2. Build with Maven:
```
cd EidGift && mvn clean package
```

3. Find the JAR in the `target/` folder.

## 🤝 Contributing

**We welcome contributions!**

- Report bugs via [GitHub Issues](https://github.com/EhsanMNA/EidGift/issues).
- Submit pull requests for features/fixes.
- Suggest ideas.

## 📜 License

MIT License - See [LICENSE](https://github.com/EhsanMNA/EidGift/blob/main/LICENSE).

## 🆘 Support

1. Check existing [issues](https://github.com/EhsanMNA/EidGift/issues) for solutions.
2. Open a new issue with:
  - Plugin version
  - Server logs (if applicable)
  - Detailed problem description

#### 🎉 Celebrate with style using EidGift!
