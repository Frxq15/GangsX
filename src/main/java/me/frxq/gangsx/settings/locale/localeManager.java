package me.frxq.gangsx.settings.locale;

import me.frxq.gangsx.GangsX;
import me.frxq.gangsx.objects.Gang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class localeManager {
    public File localeFile;
    public FileConfiguration localeConfig;
    private final GangsX plugin;

    public localeManager(GangsX plugin) {
        this.plugin = plugin;
    }

    public void createLocaleFile() {
        localeFile = new File(plugin.getDataFolder(), "locale.yml");
        if (!localeFile.exists()) {
            localeFile.getParentFile().mkdirs();
            plugin.log("locale.yml was created successfully");
            plugin.saveResource("locale.yml", false);
        }

        localeConfig = new YamlConfiguration();
        try {
            localeConfig.load(localeFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadLocaleFile() { localeConfig = YamlConfiguration.loadConfiguration(localeFile); }
    public void saveLocaleFile() {
        try {
            localeConfig.save(localeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getLocaleFile() { return localeConfig; }

    public void sendPermissionMessage(CommandSender sender) {
        sender.sendMessage(plugin.getColorFormatter().format(getLocaleFile().getString("NO_PERMISSION")));
    }
    public void sendUsageMessage(CommandSender sender, String usage) {
        sender.sendMessage(plugin.getColorFormatter().format(usage));
    }
    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(plugin.getColorFormatter().format(getLocaleFile().getString(message)));
    }
    public void sendRawMessage(CommandSender sender, String message) {
        sender.sendMessage(plugin.getColorFormatter().format(message));
    }
    public void broadcastLocaleMessage(String message) {
        Bukkit.broadcastMessage(plugin.getColorFormatter().format(getLocaleFile().getString(message)));
    }
    public void broadcastMessage(String message) {
        Bukkit.broadcastMessage(plugin.getColorFormatter().format(message));
    }
    public String getMessage(String message) {
        return plugin.getColorFormatter().format(getLocaleFile().getString(message));
    }
    public void sendGangInfo(CommandSender sender, Gang gang) {
        getLocaleFile().getStringList("GANG_INFO").forEach(line -> {
            line = line
                    .replace("%gang%", gang.getName())
                    .replace("%created%", gang.getCreationDate())
                            .replace("%leader%", Bukkit.getOfflinePlayer(gang.getLeader()).getName())
                            .replace("%level%", gang.getLevel()+"")
                            .replace("%description%", gang.getDescription())
                            .replace("%kills%", gang.getKills()+"")
                            .replace("%deaths%", gang.getDeaths()+"")
                            .replace("%blocksbroken%", gang.getBlocksMinedFormatted()+"")
                            .replace("%members%", gang.convertMembersForInfo())
                            .replace("%allies%", "&dinsert allies")
                            .replace("%coins%", gang.getCoins()+"")
                            .replace("%bank%", gang.getBalanceFormatted())
                                    .replace("%points%", gang.getPoints()+"")
                                    .replace("%totalmembers%", gang.getMembersCount()+"");
            sendRawMessage(sender, line);
        });
    }
}
