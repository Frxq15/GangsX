package me.frxq.gangsx;

import me.frxq.gangsx.api.APIHooks;
import me.frxq.gangsx.fight.ArenaManager;
import me.frxq.gangsx.listener.PlayerListeners;
import me.frxq.gangsx.command.CommandHandler;
import me.frxq.gangsx.datafactory.gang.GangDataFactory;
import me.frxq.gangsx.datafactory.player.GPlayerDataFactory;
import me.frxq.gangsx.datafactory.player.sql.SQLGPlayerDataFactory;
import me.frxq.gangsx.formatting.color.ColorFormatter;
import me.frxq.gangsx.datafactory.gang.sql.SQLGangDataFactory;
import me.frxq.gangsx.datafactory.sql.SQLHandler;
import me.frxq.gangsx.gui.GUIListeners;
import me.frxq.gangsx.leaderboard.LeaderboardManager;
import me.frxq.gangsx.listener.DataFactoryListener;
import me.frxq.gangsx.managers.FileManager;
import me.frxq.gangsx.managers.RoleManager;
import me.frxq.gangsx.objects.GPlayer;
import me.frxq.gangsx.objects.Gang;
import me.frxq.gangsx.settings.Settings;
import me.frxq.gangsx.settings.locale.localeManager;
import me.frxq.gangsx.settings.version.ServerVersion;
import me.frxq.gangsx.settings.version.ServerVersionChecker;
import me.frxq.gangsx.utils.ArenaUtils;
import me.frxq.gangsx.utils.CommandUtils;
import me.frxq.gangsx.utils.GangUtils;
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
    private ArenaUtils arenaUtils;
    private ArenaManager arenaManager;

    public static GangsX instance;


    @Override
    public void onEnable() {
        instance = this;
        // Initialization logic here
        //getConfig().options().copyDefaults(true);
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

        fileManager = new FileManager(this);
        fileManager.generate();

        localeManager = new localeManager(this);
        localeManager.createLocaleFile();

        apiHooks = new APIHooks(this);
        apiHooks.initialize();


        roleManager = new RoleManager(this);
        commandHandler = new CommandHandler(this);
        commandHandler.load();

        commandUtils = new CommandUtils();

        leaderboardManager = new LeaderboardManager(this);

        gangUtils = new GangUtils(this);
        arenaUtils = new ArenaUtils();
        arenaManager = new ArenaManager(this);


        switch (settings.getStorageType()) {
            case MYSQL:
                sqlSetup();
                break;
            case MONGODB:
                getLogger().warning("MongoDB not supported yet.");
                break;
        }

        Bukkit.getPluginManager().registerEvents(new DataFactoryListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GUIListeners(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);

        sVersionChecker = new ServerVersionChecker();

        if (sVersionChecker.isServerAbove(ServerVersion.VERSION_1_16)) {
            log("Assigning to spigot version: "+sVersionChecker.getVersion());
            colorFormatter = new ColorFormatter();
        }

        else if (sVersionChecker.isServerAbove(ServerVersion.LEGACY)) {
            log("Assigning to spigot version: "+sVersionChecker.getVersion()+" (LEGACY BUILD)");
            colorFormatter = new ColorFormatter();
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

    public ArenaManager getArenaManager() { return arenaManager; }

    public CommandUtils getCommandUtils() { return commandUtils; }

    public RoleManager getRoleManager() { return roleManager; }

    public LeaderboardManager getLeaderboardManager() { return leaderboardManager; }

    public ArenaUtils getArenaUtils() { return arenaUtils; }

    public GangUtils getGangUtils() { return gangUtils; }

    public APIHooks getAPIHooks() { return apiHooks; }

}