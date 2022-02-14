package net.withery.gangsx;

import net.withery.gangsx.Utils.FileManager;
import net.withery.gangsx.Utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class GangsX extends JavaPlugin {

    private static GangsX instance;
    private Settings settings;
    private FileManager fileManager;

    @Override
    public void onEnable() {
        // Initialization logic here
        instance = this;
        saveDefaultConfig();
        registry();

        getLogger().info("Enabled " + getDescription().getName() + " v" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        // Shutdown logic here
        getLogger().info("Disabled " + getDescription().getName() + " v" + getDescription().getVersion());
    }

    public void log(String text) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[GangsX] " + text);
    }

    void registry() {
        settings = new Settings(this);
        settings.setSettings();

        fileManager = new FileManager(this);
        fileManager.generateMessages();
    }

    public static GangsX getInstance() {
        return instance;
    }

    public Settings getSettings() {
        return settings;
    }

}