package net.withery.gangsx;

import net.withery.gangsx.api.APIHooks;
import net.withery.gangsx.command.CommandHandler;
import net.withery.gangsx.formatting.color.ColorFormatter;
import net.withery.gangsx.datafactory.gang.sql.SQLGangDataFactory;
import net.withery.gangsx.datafactory.sql.SQLHandler;
import net.withery.gangsx.formatting.color.colorformatter.ColorFormatter_1_16;
import net.withery.gangsx.formatting.color.colorformatter.ColorFormatter_LEGACY;
import net.withery.gangsx.managers.FileManager;
import net.withery.gangsx.settings.Settings;
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
    private APIHooks apiHooks;
    private FileManager fileManager;

    @Override
    public void onEnable() {
        // Initialization logic here
        instance = this;
        saveDefaultConfig();
        registry();
        if (sqlGangDataFactory == null) return;
        log("Enabled " + getDescription().getName() + " v" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        // Shutdown logic here
        log("Disabled " + getDescription().getName() + " v" + getDescription().getVersion());
    }

    public void log(String text) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[GangsX] " + text);
    }

    private void registry() {
        // Move loading of things to different class/methods
        settings = new Settings(this);

        localeRegistry = new LocaleRegistry(this);
        localeRegistry.load();

        CommandHandler commandHandler = new CommandHandler(this);
        commandHandler.load();

        apiHooks = new APIHooks(this);
        apiHooks.initialize();

        fileManager = new FileManager(this);
        fileManager.createShopFile();

        switch (settings.getStorageType()) {
            case MYSQL, MONGODB -> sqlSetup();
        }

        /*if (sqlGangDataFactory == null) {
            log("yeah bro its null");
            return;
        }*/

        sVersionChecker = new ServerVersionChecker();

        if (sVersionChecker.isServerAbove(ServerVersion.VERSION_1_16)) {
            log("set to 1.16");
            colorFormatter = new ColorFormatter_1_16();
        }

        else if (sVersionChecker.isServerAbove(ServerVersion.LEGACY)) {
            log("set to legacy");
            colorFormatter = new ColorFormatter_LEGACY();
        }

        else {
            // Throw error, shutdown logic
        }
    }

    public void sqlSetup() {
        SQLHandler sqlHandler = new SQLHandler(this, settings.getHost(), settings.getDatabase(), settings.getUsername(), settings.getPassword(), settings.getPort());
        if (!sqlHandler.connect()) {
            log("Connection to mysql failed.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // TODO: 14/04/2022 read table prefix from config
        sqlGangDataFactory = new SQLGangDataFactory(this, sqlHandler, "gangsx_");
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

    public FileManager getFileManager() { return fileManager; }

    public LocaleRegistry getLocaleRegistry() {
        return localeRegistry;
    }

}