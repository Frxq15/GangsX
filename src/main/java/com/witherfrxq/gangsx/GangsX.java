package com.witherfrxq.gangsx;

import com.witherfrxq.gangsx.Utils.FileManager;
import com.witherfrxq.gangsx.Utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class GangsX extends JavaPlugin {
    private static GangsX instance;
    private Settings settings;
    private FileManager fileManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        registry();

    }
    void registry() {
        settings = new Settings(this);
        settings.setSettings();

        fileManager = new FileManager(this);
        fileManager.generateMessages();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void log(String text) { Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"[GangsX] "+text); }

    public static GangsX getInstance() { return instance; }

    public Settings getSettings() { return settings; }
}
