package net.withery.gangsx.Utils;

import net.withery.gangsx.GangsX;

public class Settings {

    private final GangsX plugin;
    private String storageType;

    public Settings(GangsX plugin) {
        this.plugin = plugin;
    }

    public void setSettings() {
        checkStorage();
    }

    public void checkStorage() {
        storageType = plugin.getConfig().getString("storage-method", "null");
        switch (storageType.toLowerCase()) {
            case "mysql":
                plugin.log("Using storage type MySQL");
                plugin.sqlSetup();
                return;
            case "mongodb":
                plugin.log("Using storage type 'MongoDB'");
                return;
            default:
                plugin.log("Please set a valid storage type in the 'storage-method' section of the config.yml");
                plugin.getPluginLoader().disablePlugin(plugin);
                return;
        }
    }

}