package com.witherfrxq.gangsx.Utils;

import com.witherfrxq.gangsx.GangsX;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {
    private final GangsX plugin;
    public File LangFile;
    public FileConfiguration LangConfig;

    public FileManager(GangsX plugin) {
        this.plugin = plugin;
    }

    public void generateMessages() {
        LangFile = new File(plugin.getInstance().getDataFolder(), "messages.yml");
        if (!LangFile.exists()) {
            LangFile.getParentFile().mkdirs();
            plugin.log("messages.yml was created successfully");
            plugin.getInstance().saveResource("messages.yml", false);
        }

        LangConfig = new YamlConfiguration();
        try {
            LangConfig.load(LangFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadMessages() { LangConfig = YamlConfiguration.loadConfiguration(LangFile); }

    public void saveMessages() {
        try {
            LangConfig.save(LangFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getMessages() { return LangConfig; }

}
