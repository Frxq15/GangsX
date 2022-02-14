package com.witherfrxq.gangsx;

import com.witherfrxq.gangsx.Utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class GangsX extends JavaPlugin {
    private static GangsX instance;
    private Settings settings;

    @Override
    public void onEnable() {
        instance = this;
        registry();

    }
    void registry() {
        settings = new Settings(this);
        settings.setSettings();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void log(String text) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"[GangsX] "+text);}

        public Settings getSettings() {
            return settings;
        }
}
