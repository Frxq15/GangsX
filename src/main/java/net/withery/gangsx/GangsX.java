package net.withery.gangsx;

import net.withery.gangsx.api.APIHooks;
import net.withery.gangsx.command.CommandHandler;
import net.withery.gangsx.datafactory.gang.GangDataFactory;
import net.withery.gangsx.datafactory.player.GPlayerDataFactory;
import net.withery.gangsx.datafactory.player.sql.SQLGPlayerDataFactory;
import net.withery.gangsx.formatting.color.ColorFormatter;
import net.withery.gangsx.datafactory.gang.sql.SQLGangDataFactory;
import net.withery.gangsx.datafactory.sql.SQLHandler;
import net.withery.gangsx.formatting.color.colorformatter.ColorFormatter_1_16;
import net.withery.gangsx.formatting.color.colorformatter.ColorFormatter_LEGACY;
import net.withery.gangsx.formatting.number.NumberFormatter;
import net.withery.gangsx.gui.GUIListeners;
import net.withery.gangsx.leaerboard.LeaderboardManager;
import net.withery.gangsx.listener.DataFactoryListener;
import net.withery.gangsx.managers.FileManager;
import net.withery.gangsx.managers.RoleManager;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import net.withery.gangsx.settings.Settings;
import net.withery.gangsx.settings.locale.localeManager;
import net.withery.gangsx.settings.version.ServerVersion;
import net.withery.gangsx.settings.version.ServerVersionChecker;
import net.withery.gangsx.utils.CommandUtils;
import net.withery.gangsx.utils.GangUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class GangsX extends JavaPlugin {

    private Settings settings;
    private ServerVersionChecker sVersionChecker;
    private ColorFormatter colorFormatter;
    private localeManager localeManager;
    private GangDataFactory gangDataFactory;
    private GPlayerDataFactory gPlayerDataFactory;
    private APIHooks apiHooks;
    private FileManager fileManager;
    private CommandHandler commandHandler;
    private RoleManager roleManager;
    private CommandUtils commandUtils;
    private LeaderboardManager leaderboardManager;

    private GangUtils gangUtils;

    public static GangsX instance;


    @Override
    public void onEnable() {
        instance = this;
        // Initialization logic here
        saveDefaultConfig();
        registry();
        if (gangDataFactory == null) return;
        log("Enabled " + getDescription().getName() + " v" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        gangDataFactory.terminate();
        gPlayerDataFactory.terminate();
        // Shutdown logic here
        log("Disabled " + getDescription().getName() + " v" + getDescription().getVersion());
    }

    public void log(String text) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[GangsX] " + text);
    }

    private void registry() {
        // Move loading of things to different class/methods
        settings = new Settings(this);

        localeManager = new localeManager(this);
        localeManager.createLocaleFile();

        apiHooks = new APIHooks(this);
        apiHooks.initialize();

        fileManager = new FileManager(this);
        fileManager.createShopFile();
        fileManager.createTopFile();

        roleManager = new RoleManager(this);
        commandHandler = new CommandHandler(this);
        commandHandler.load();

        commandUtils = new CommandUtils();

        leaderboardManager = new LeaderboardManager(this);

        gangUtils = new GangUtils(this);

        Bukkit.getPluginManager().registerEvents(new GUIListeners(), this);

        switch (settings.getStorageType()) {
            case MYSQL -> sqlSetup();
            case MONGODB -> getLogger().warning("MongoDB not supported yet.");
        }

        Bukkit.getPluginManager().registerEvents(new DataFactoryListener(this), this);

        sVersionChecker = new ServerVersionChecker();

        if (sVersionChecker.isServerAbove(ServerVersion.VERSION_1_16)) {
            log("Assigning to spigot version: "+sVersionChecker.getVersion());
            colorFormatter = new ColorFormatter_1_16();
        }

        else if (sVersionChecker.isServerAbove(ServerVersion.LEGACY)) {
            log("Assigning to spigot version: "+sVersionChecker.getVersion()+" (LEGACY BUILD)");
            colorFormatter = new ColorFormatter_LEGACY();
        }

        else {
            // Throw error, shutdown logic
        }
    }

    public void sqlSetup() {
        SQLHandler sqlHandler = new SQLHandler(this, settings.getHost(), settings.getDatabase(), settings.getUsername(), settings.getPassword(), settings.getPort());
        if (!sqlHandler.connect()) {
            log("MySQL Connection: failed");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // TODO: 14/04/2022 read table prefix from config
        gangDataFactory = new SQLGangDataFactory(this, sqlHandler, "gangsx_");
        getGangDataFactory().initialize();
        gPlayerDataFactory = new SQLGPlayerDataFactory(this, sqlHandler, "gangsx_");
        getGPlayerDataFactory().initialize();
        Bukkit.getScheduler().runTaskLater(getInstance(), () -> getGangDataFactory().updateLeaderboardTopValues(), 20L * 3);

        // NOTE: fixes online members in instance of plugin reload.
        Bukkit.getScheduler().runTaskLater(getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                GPlayer gPlayer = getGPlayerDataFactory().getGPlayerData(player.getUniqueId());
                if(gPlayer.hasGang()) {
                    Gang gang = getGangDataFactory().getGangData(gPlayer.getGangId());
                        if(!gang.getOnlineMembers().contains(gPlayer)) {
                            gang.addOnlineMember(gPlayer);
                    }
                }
            });
        }, 20L * 3);
        log("MySQL Connection: success");
    }
    public static GangsX getInstance() { return instance; }

    public Settings getSettings() {
        return settings;
    }


    public ColorFormatter getColorFormatter() {
        return colorFormatter;
    }

    public GangDataFactory getGangDataFactory() {
        return gangDataFactory;
    }

    public GPlayerDataFactory getGPlayerDataFactory() {
        return gPlayerDataFactory;
    }

    public FileManager getFileManager() { return fileManager; }

    public localeManager getLocaleManager() {
        return localeManager;
    }

    public CommandUtils getCommandUtils() { return commandUtils; }

    public RoleManager getRoleManager() { return roleManager; }

    public LeaderboardManager getLeaderboardManager() { return leaderboardManager; }

    public GangUtils getGangUtils() { return gangUtils; }

    public APIHooks getAPIHooks() { return apiHooks; }

}