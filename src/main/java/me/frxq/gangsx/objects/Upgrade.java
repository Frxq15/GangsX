package me.frxq.gangsx.objects;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.enums.Upgrades;
import org.bukkit.configuration.file.FileConfiguration;

public class Upgrade {
    private final GangsX plugin;
    private final Gang gang;
    private final Upgrades upgrade;

    private FileConfiguration file;
    private int level, cost;
    private boolean isMaximumLevel;


    public Upgrade(GangsX plugin, Gang gang, Upgrades upgrade) {
        this.plugin = plugin;
        this.gang = gang;
        this.upgrade = upgrade;
        file = plugin.getConfig();
    }

    public int getLevel() { return level; }

    public int getNextlevel() {
        if(getLevel()+1 >= getMaximumLevel()) return 0;
        return (getLevel()+1);
    }

    public int getMaximumLevel() {
        return file.getInt("upgrades."+upgrade.getName()+".maximum_level");
    }

    public int getUpgradeCost() {
        if(isMaximumLevel()) return 0;
        return cost;
    }

    public boolean isMaximumLevel() { return isMaximumLevel; }

    public void performUpgrade() {}

    public void setLevel(Integer level) {
        this.level = level;
    }
}
