package me.ehsanmna.eidGift.config;

import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;

public class ParticleConfig {
    private final Particle type;
    private final int count;

    public ParticleConfig(ConfigurationSection config) {
        this.type = Particle.valueOf(config.getString("type", "VILLAGER_HAPPY").toUpperCase());
        this.count = config.getInt("count", 10);
    }

    public Particle getType() {
        return type;
    }

    public int getCount() {
        return count;
    }
}