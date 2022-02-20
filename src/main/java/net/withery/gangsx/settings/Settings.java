package net.withery.gangsx.settings;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.settings.storage.StorageType;

import java.util.Locale;

public class Settings {

    private final GangsX plugin;
    private String host, database, username, password;
    private int port;

    public Settings(GangsX plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public StorageType getStorageType() {
        StorageType storageType;
        switch (plugin.getConfig().getString("storage-method", "mariadb").toLowerCase(Locale.ENGLISH)) {
            case "mysql" -> {
                storageType = StorageType.MYSQL;
                setSqlDetails();
            }
            case "mariadb" -> storageType = StorageType.MARIADB;
            case "mongodb" -> storageType = StorageType.MONGODB;
            default -> storageType = null;
        }

        return storageType;
    }

    public void reload() {
        plugin.reloadConfig();
    }

    public void setSqlDetails() {
        host = plugin.getConfig().getString("database.mysql.host");
        database = plugin.getConfig().getString("database.mysql.database");
        username = plugin.getConfig().getString("database.mysql.username");
        password = plugin.getConfig().getString("database.mysql.password");
        port = plugin.getConfig().getInt("database.mysql.port");
    }
    public String getHost() {
        return host;
    }
    public String getDatabase() {
        return database;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public int getPort() {
        return port;
    }
}