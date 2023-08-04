package net.withery.gangsx.managers;

import net.withery.gangsx.GangsX;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {
    private final GangsX plugin;
    public File ShopFile;
    public FileConfiguration ShopConfig;

    public File TopFile;
    public FileConfiguration TopConfig;

    public File ValueFile;
    public FileConfiguration ValueConfig;

    public FileManager(GangsX plugin) {
        this.plugin = plugin;
    }

    public void generate() {
        createShopFile();
        createValueFile();
        createTopFile();
    }

    public void createShopFile() {
        ShopFile = new File(plugin.getDataFolder()+"/guis", "shop.yml");
        if (!ShopFile.exists()) {
            ShopFile.getParentFile().mkdirs();
            plugin.log("shop.yml was created successfully");
            plugin.saveResource("guis/shop.yml", false);
        }
        ShopConfig = new YamlConfiguration();
        try {
            ShopConfig.load(ShopFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadShopFile() { ShopConfig = YamlConfiguration.loadConfiguration(ShopFile); }
    public void saveShopFile() {
        try {
            ShopConfig.save(ShopFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getShopFile() { return ShopConfig; }

    public void createTopFile() {
        TopFile = new File(plugin.getDataFolder()+"/guis", "gangtop.yml");
        if (!TopFile.exists()) {
            TopFile.getParentFile().mkdirs();
            plugin.log("gangtop.yml was created successfully");
            plugin.saveResource("guis/gangtop.yml", false);
        }
        TopConfig = new YamlConfiguration();
        try {
            TopConfig.load(TopFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadTopFile() { TopConfig = YamlConfiguration.loadConfiguration(TopFile); }
    public void saveTopFile() {
        try {
            TopConfig.save(TopFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getTopFile() { return TopConfig; }

    public void createValueFile() {
        ValueFile = new File(plugin.getDataFolder()+"/guis", "value.yml");
        if (!ValueFile.exists()) {
            ValueFile.getParentFile().mkdirs();
            plugin.log("gangValue.yml was created successfully");
            plugin.saveResource("guis/value.yml", false);
        }
        ValueConfig = new YamlConfiguration();
        try {
            ValueConfig.load(ValueFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadValueFile() { ValueConfig = YamlConfiguration.loadConfiguration(ValueFile); }
    public void saveValueFile() {
        try {
            ValueConfig.save(ValueFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getValueFile() { return ValueConfig; }

}
