package me.frxq.gangsx.managers;

import me.frxq.gangsx.GangsX;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {
    private final GangsX plugin;
    public File ShopFile;
    public FileConfiguration ShopConfig;

    public File permissionsFile;
    public FileConfiguration permissionsConfig;

    public File permissionsManagerFile;
    public FileConfiguration permissionsManagerConfig;

    public File TopFile;
    public FileConfiguration TopConfig;

    public File ValueFile;
    public FileConfiguration ValueConfig;

    public File InvseeFile;
    public FileConfiguration InvseeConfig;

    public File LevelFile;
    public FileConfiguration LevelConfig;
    public File DisbandFile;
    public FileConfiguration DisbandConfig;

    public File ArenaFile;
    public FileConfiguration ArenaConfig;

    public File RosterFile;
    public FileConfiguration RosterConfig;
    public File addToRosterFile;
    public FileConfiguration addToRosterConfig;

    public FileManager(GangsX plugin) {
        this.plugin = plugin;
    }

    public void generate() {
        createShopFile();
        createValueFile();
        createTopFile();
        createPermissionsFile();
        createPermissionsManagerFile();
        createInvseeFile();
        createLevelFile();
        createDisbandFile();
        createArenaFile();
        createRosterFile();
        createAddToRosterFile();
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
            plugin.log("value.yml was created successfully");
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

    public void createPermissionsFile() {
        permissionsFile = new File(plugin.getDataFolder()+"/guis", "permissions.yml");
        if (!permissionsFile.exists()) {
            permissionsFile.getParentFile().mkdirs();
            plugin.log("permissions.yml was created successfully");
            plugin.saveResource("guis/permissions.yml", false);
        }
        permissionsConfig = new YamlConfiguration();
        try {
            permissionsConfig.load(permissionsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadPermissionsFile() { permissionsConfig = YamlConfiguration.loadConfiguration(permissionsFile); }
    public void savePermissionsFile() {
        try {
            permissionsConfig.save(permissionsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getPermissionsFile() { return permissionsConfig; }

    public void createPermissionsManagerFile() {
        permissionsManagerFile = new File(plugin.getDataFolder()+"/guis", "permissions-select.yml");
        if (!permissionsManagerFile.exists()) {
            permissionsManagerFile.getParentFile().mkdirs();
            plugin.log("permissionss-select.yml was created successfully");
            plugin.saveResource("guis/permissions-select.yml", false);
        }
        permissionsManagerConfig = new YamlConfiguration();
        try {
            permissionsManagerConfig.load(permissionsManagerFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadPermissionsManagerFile() { permissionsManagerConfig = YamlConfiguration.loadConfiguration(permissionsManagerFile); }
    public void savePermissionsManagerFile() {
        try {
            permissionsManagerConfig.save(permissionsManagerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getPermissionsManagerFile() { return permissionsManagerConfig; }

    public void createInvseeFile() {
        InvseeFile = new File(plugin.getDataFolder()+"/guis", "invsee.yml");
        if (!InvseeFile.exists()) {
            InvseeFile.getParentFile().mkdirs();
            plugin.log("invsee.yml was created successfully");
            plugin.saveResource("guis/invsee.yml", false);
        }
        InvseeConfig = new YamlConfiguration();
        try {
            InvseeConfig.load(InvseeFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadInvseeFile() { InvseeConfig = YamlConfiguration.loadConfiguration(InvseeFile); }
    public void saveInvseeFile() {
        try {
            InvseeConfig.save(InvseeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getInvseeFile() { return InvseeConfig; }

    public void createLevelFile() {
        LevelFile = new File(plugin.getDataFolder()+"/guis", "level.yml");
        if (!LevelFile.exists()) {
            LevelFile.getParentFile().mkdirs();
            plugin.log("level.yml was created successfully");
            plugin.saveResource("guis/level.yml", false);
        }
        LevelConfig = new YamlConfiguration();
        try {
            LevelConfig.load(LevelFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadLevelFile() { LevelConfig = YamlConfiguration.loadConfiguration(LevelFile); }
    public void saveLevelFile() {
        try {
            LevelConfig.save(LevelFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getLevelFile() { return LevelConfig; }

    public void createDisbandFile() {
        DisbandFile = new File(plugin.getDataFolder()+"/guis", "disband.yml");
        if (!DisbandFile.exists()) {
            DisbandFile.getParentFile().mkdirs();
            plugin.log("disband.yml was created successfully");
            plugin.saveResource("guis/disband.yml", false);
        }
        DisbandConfig = new YamlConfiguration();
        try {
            DisbandConfig.load(DisbandFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadDisbandFile() { DisbandConfig = YamlConfiguration.loadConfiguration(DisbandFile); }
    public void saveDisbandFile() {
        try {
            DisbandConfig.save(DisbandFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getDisbandFile() { return DisbandConfig; }

    public void createArenaFile() {
        ArenaFile = new File(plugin.getDataFolder(), "arena.yml");
        if (!ArenaFile.exists()) {
            ArenaFile.getParentFile().mkdirs();
            plugin.log("arena.yml was created successfully");
            plugin.saveResource("arena.yml", false);
        }

        ArenaConfig = new YamlConfiguration();
        try {
            ArenaConfig.load(ArenaFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadArenaFile() { ArenaConfig = YamlConfiguration.loadConfiguration(ArenaFile); }
    public void saveArenaFile() {
        try {
            ArenaConfig.save(ArenaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getArenaFile() { return ArenaConfig; }

    public void createRosterFile() {
        RosterFile = new File(plugin.getDataFolder()+"/guis", "roster.yml");
        if (!RosterFile.exists()) {
            RosterFile.getParentFile().mkdirs();
            plugin.log("roster.yml was created successfully");
            plugin.saveResource("guis/roster.yml", false);
        }
        RosterConfig = new YamlConfiguration();
        try {
            RosterConfig.load(RosterFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadRosterFile() { RosterConfig = YamlConfiguration.loadConfiguration(RosterFile); }
    public void saveRosterFile() {
        try {
            RosterConfig.save(RosterFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getRosterFile() { return RosterConfig; }

    public void createAddToRosterFile() {
        addToRosterFile = new File(plugin.getDataFolder()+"/guis", "add-to-roster.yml");
        if (!addToRosterFile.exists()) {
            addToRosterFile.getParentFile().mkdirs();
            plugin.log("add-to-roster.yml was created successfully");
            plugin.saveResource("guis/add-to-roster.yml", false);
        }
        addToRosterConfig = new YamlConfiguration();
        try {
            addToRosterConfig.load(addToRosterFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadAddToRosterFile() { addToRosterConfig = YamlConfiguration.loadConfiguration(addToRosterFile); }
    public void saveAddToRosterFile() {
        try {
            addToRosterConfig.save(addToRosterFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getAddToRosterFile() { return addToRosterConfig; }
}
