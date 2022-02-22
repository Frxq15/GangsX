package net.withery.gangsx.managers;

import net.withery.gangsx.GangsX;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {
    private final GangsX plugin;

    public File LangFile;
    public FileConfiguration LangConfig;
    public File ShopFile;
    public FileConfiguration ShopConfig;

    public FileManager(GangsX plugin) {
        this.plugin = plugin;
    }

    public void createShopFile() {
        ShopFile = new File(plugin.getDataFolder()+"/guis", "shop.yml");
        if (!ShopFile.exists()) {
            ShopFile.getParentFile().mkdirs();
            plugin.log("shop.yml was created successfully");
            plugin.getInstance().saveResource("guis/shop.yml", false);
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

    public void createLangFile() {
        LangFile = new File(plugin.getDataFolder(), "en-us.yml");
        if (!LangFile.exists()) {
            LangFile.getParentFile().mkdirs();
            plugin.log("en-us.yml was created successfully");
            plugin.getInstance().saveResource("en-us.yml", false);
        }

        LangConfig = new YamlConfiguration();
        try {
            LangConfig.load(LangFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadLangFile() { LangConfig = YamlConfiguration.loadConfiguration(LangFile); }
    public void saveLangFile() {
        try {
            LangConfig.save(LangFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getLangFile() { return LangConfig; }
}
