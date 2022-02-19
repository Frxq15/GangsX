package net.withery.gangsx;

import net.withery.gangsx.CommandManager.CommandHandler;
import net.withery.gangsx.Utils.Settings;
import net.withery.gangsx.datafactory.gang.SQLGangDataFactory;
import net.withery.gangsx.datafactory.sql.SQLManager;
import net.withery.gangsx.formatting.color.ColorFormatter;
import net.withery.gangsx.formatting.color.colorformatter.ColorFormatter_1_16;
import net.withery.gangsx.formatting.color.colorformatter.ColorFormatter_LEGACY;
import net.withery.gangsx.settings.locale.LocaleRegistry;
import net.withery.gangsx.settings.version.ServerVersion;
import net.withery.gangsx.settings.version.ServerVersionChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class GangsX extends JavaPlugin {

    private static GangsX instance;
    private Settings settings;
    private ServerVersionChecker sVersionChecker;
    private ColorFormatter colorFormatter;
    private LocaleRegistry localeRegistry;
    private SQLGangDataFactory sqlGangDataFactory;

    @Override
    public void onEnable() {
        // Initialization logic here
        instance = this;
        saveDefaultConfig();
        registry();
        if(sqlGangDataFactory == null) return;

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
        // Move loading of things to different class/methods
        settings = new Settings(this);
        settings.setSettings();

        if(sqlGangDataFactory == null) return;

        sVersionChecker = new ServerVersionChecker();

        if (sVersionChecker.isServerAtLeast(ServerVersion.VERSION_1_16))
            colorFormatter = new ColorFormatter_1_16();

        else if (sVersionChecker.isServerAtLeast(ServerVersion.LEGACY))
            colorFormatter = new ColorFormatter_LEGACY();

        else {
            // Throw error, shutdown logic
        }

        localeRegistry = new LocaleRegistry(this);
        localeRegistry.load();

        CommandHandler commandHandler = new CommandHandler(this);
        commandHandler.load();
    }

    public void sqlSetup() {
        String host = getConfig().getString("database.mysql.host");
        String database = getConfig().getString("database.mysql.database");
        String username = getConfig().getString("database.mysql.username");
        String password = getConfig().getString("database.mysql.password");
        int port = getConfig().getInt("database.mysql.port");

        SQLManager sqlManager = new SQLManager(this, host, database, username, password, port);
       if(!sqlManager.connect()) {
            log("Connection to mysql failed.");
            Bukkit.getPluginManager().disablePlugin(this);
       }

    }

    public static GangsX getInstance() {
        return instance;
    }

    public Settings getSettings() {
        return settings;
    }

    public ColorFormatter getColorFormatter() {
        return colorFormatter;
    }

    public LocaleRegistry getLocaleRegistry() {
        return localeRegistry;
    }

}