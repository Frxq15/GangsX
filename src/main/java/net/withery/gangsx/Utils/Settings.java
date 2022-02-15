package net.withery.gangsx.Utils;

import net.withery.gangsx.GangsX;

public class Settings {

    private final GangsX plugin;
    private String storagetype;

    public Settings(GangsX plugin) {
        this.plugin = plugin;
    }

    public void setSettings() {
        checkStorage();
    }

    public void checkStorage() {
        this.storagetype = plugin.getConfig().getString("storage-method");
        switch (storagetype.toLowerCase()) {
            case "mysql":
                plugin.log("Using storage type 'MySQL'");
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