package net.withery.gangsx.settings;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.settings.storage.StorageType;

import java.util.Locale;

public class Settings {

    private final GangsX plugin;

    public Settings(GangsX plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public StorageType getStorageType() {
        StorageType storageType;
        switch (plugin.getConfig().getString("storage-method", "mariadb").toLowerCase(Locale.ENGLISH)) {
            case "mysql" -> storageType = StorageType.MYSQL;
            case "mariadb" -> storageType = StorageType.MARIADB;
            case "mongodb" -> storageType = StorageType.MONGODB;
            default -> storageType = null;
        }

        return storageType;
    }

    public void reload() {
        plugin.reloadConfig();
    }

}