package com.magnet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class Magnet {

    private final YamlConfiguration config;
    private final String name;
    private final List<String> lore;
    private final double range;
    private final double speed;
    private final String tier;
    private final Material material;
    private final String id;
    private final int modelData;
    private final List<String> effects;
    private final boolean hasFilter;
    private final List<Material> filter;
    private final Particle particle;
    public Magnet(File file) {
        this.config = YamlConfiguration.loadConfiguration(file);

        this.name = ChatColor.translateAlternateColorCodes('&', this.config.getString("name"));
        this.lore = this.config.getStringList("lore").stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
        this.range = this.config.getDouble("range");
        this.speed = this.config.getDouble("speed");
        this.tier = ChatColor.translateAlternateColorCodes('&', this.config.getString("tier"));
        this.material = Material.valueOf(this.config.getString("material"));
        this.id = file.getName().replace(".yml", "");
        this.modelData = this.config.getInt("model-data");
        this.effects = this.config.getStringList("effects");
        this.hasFilter = this.config.getBoolean("has-filter");
        this.filter = this.config.getStringList("filter").stream()
                .map(Material::valueOf)
                .collect(Collectors.toList());
        this.particle = this.config.getString("particle").equalsIgnoreCase("NONE") ? null : Particle.valueOf(this.config.getString("particle"));
    }


    public Particle getParticle() {
        return particle;
    }

    public String getId() {
        return id;
    }

    public int getModelData() {
        return modelData;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public double getRange() {
        return range;
    }

    public Material getMaterial() {
        return material;
    }

    public double getSpeed() {
        return speed;
    }

    public List<Material> getFilter() {
        return filter;
    }

    public boolean hasFilter() {
        return hasFilter;
    }

    public String getTier() {
        return tier;
    }

    public List<String> getEffects(){
        return effects;
    }
}
