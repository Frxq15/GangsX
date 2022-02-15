package net.withery.gangsx;

import net.withery.gangsx.CommandManager.CommandHandler;
import net.withery.gangsx.Utils.FileManager;
import net.withery.gangsx.Utils.Settings;
import net.withery.gangsx.formatting.color.ColorFormatter;
import net.withery.gangsx.formatting.color.colorformatter.ColorFormatter_1_16;
import net.withery.gangsx.formatting.color.colorformatter.ColorFormatter_LEGACY;
import net.withery.gangsx.settings.version.ServerVersion;
import net.withery.gangsx.settings.version.ServerVersionChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class GangsX extends JavaPlugin {

    private static GangsX instance;
    private Settings settings;
    private FileManager fileManager;
    private ServerVersionChecker sVersionChecker;
    private ColorFormatter colorFormatter;

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

        sVersionChecker = new ServerVersionChecker();

        if (sVersionChecker.isServerAtLeast(ServerVersion.VERSION_1_16))
            colorFormatter = new ColorFormatter_1_16();

        else if (sVersionChecker.isServerAtLeast(ServerVersion.LEGACY))
            colorFormatter = new ColorFormatter_LEGACY();

        else {
            // Throw error, shutdown logic
        }

        CommandHandler commandHandler = new CommandHandler(this);
        commandHandler.load();
    }

    public static GangsX getInstance() {
        return instance;
    }

    public Settings getSettings() {
        return settings;
    }

}