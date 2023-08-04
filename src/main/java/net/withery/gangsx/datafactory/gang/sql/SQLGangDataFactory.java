package net.withery.gangsx.datafactory.gang.sql;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.datafactory.gang.GangDataFactory;
import net.withery.gangsx.datafactory.sql.SQLHandler;
import net.withery.gangsx.objects.GPlayer;
import net.withery.gangsx.objects.Gang;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SQLGangDataFactory extends GangDataFactory {

    private final static Map<UUID, Gang> gangs = new HashMap<>();

    private final SQLHandler sqlHandler;
    private final String GANGS_TABLE;
    private int savingTask;

    private int leaderboardTask;

    private final String PLAYERS_TABLE;

    public SQLGangDataFactory(GangsX plugin, SQLHandler sqlHandler, String prefix) {
        super(plugin);
        this.sqlHandler = sqlHandler;
        this.GANGS_TABLE = prefix + "gangs";
        this.PLAYERS_TABLE = prefix + "players";
    }

    @Override
    public boolean initialize() {
        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return false;
        }

        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + GANGS_TABLE + " " +
                "(uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(16), created BIGINT, leader VARCHAR(36), level INT, " +
                "coins INT, bankBalance DOUBLE, kills INT, deaths INT, blocksbroken INT, friendlyFire BOOLEAN, description VARCHAR(36), gang_value BIGINT(132));")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        savingTask = startSavingTask();
        leaderboardTask = plugin.getLeaderboardManager().startUpdateTask();
        return true;
    }

    @Override
    public void terminate() {
        Bukkit.getScheduler().cancelTask(savingTask);
        Bukkit.getScheduler().cancelTask(leaderboardTask);
        savingTask = 0;

        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return;
        }

        for (Gang gang : gangs.values())
            updateGangData(gang);

        gangs.clear();
    }

    @Override
    public void initializeGangData(Gang gang) {
        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return;
        }

        if (doesGangDataExist(gang.getID())) return;
        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("INSERT INTO " + GANGS_TABLE + " " +
                "(uuid, name, created, leader, level, coins, bankBalance, kills, deaths, blocksbroken, friendlyFire, description, gang_value) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)")) {
            statement.setString(1, (gang.getID() == null ? null : gang.getID().toString()));
            statement.setString(2, gang.getName());
            statement.setLong(3, gang.getCreated());
            statement.setString(4, (gang.getLeader() == null ? null : gang.getLeader().toString()));
            statement.setInt(5, gang.getLevel());
            statement.setInt(6, gang.getCoins());
            statement.setDouble(7, gang.getBankBalance());
            statement.setInt(8, gang.getKills());
            statement.setInt(9, gang.getDeaths());
            statement.setInt(10, gang.getBlocksBroken());
            statement.setBoolean(11, gang.hasFriendlyFire());
            statement.setString(12, plugin.getConfig().getString("gang.default_description"));
            statement.setLong(13, 0);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!gangs.containsKey(gang.getID()))
            gangs.put(gang.getID(), gang);
    }

    @Override
    public void deleteGangData(UUID uuid) {
        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return;
        }

        if (!doesGangDataExist(uuid)) return;
        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("DELETE * FROM " + GANGS_TABLE + " WHERE uuid=?")) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        gangs.remove(uuid);
    }

    @Override
    public Gang getGangData(UUID uuid) {
        if (uuid == null) return null;

        if (gangs.get(uuid) != null)
            return gangs.get(uuid);

        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return null;
        }

        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("SELECT * FROM " + GANGS_TABLE + " WHERE uuid=?")) {
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();
            Gang gang = null;

            if (rs.next()) {
                gang = new Gang(plugin, UUID.fromString(rs.getString("uuid")), rs.getString("name"), rs.getString("description"),
                        rs.getLong("created"), UUID.fromString(rs.getString("leader")), rs.getInt("level"),
                        rs.getInt("coins"), rs.getDouble("bankBalance"), rs.getInt("kills"), rs.getInt("deaths"),
                        rs.getInt("blocksbroken"), rs.getBoolean("friendlyFire"), null, new ArrayList<GPlayer>(), new ArrayList<GPlayer>(), null, rs.getLong("gang_value"));

                gang.importMembers(getGangMembers(UUID.fromString(rs.getString("uuid"))));
                // TODO: 17/04/2022 get upgrades/members/allies

                // TODO: 17/04/2022 check how to get table names from each others datafactory
                // Members: SELECT uuid FROM `*players_table*` WHERE gang=?;
                // Allies: SELECT ally FROM `*allies_table*` WHERE gang=?; (Requires another table (gang - ally) which is using a composite primary key)
                // Upgrades: SELECT * FROM `*upgrades_table*` WHERE gang=?; (Could be done within the gangs table but just to minimize having giant tables id recommend making another one (gang - upgrade1 - upgrade2 - upgrade3 - etc.)
                if (!gangs.containsKey(gang.getID()))
                    gangs.put(gang.getID(), gang);
            }

            rs.close();
            return gang;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateGangData(Gang gang) {
        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return;
        }

        // Insert if not exists, update if exists
        final String UPDATE_DATA = "INSERT INTO `" + GANGS_TABLE + "` (uuid, name, created, leader, level, coins, bankBalance, kills, deaths, blocksbroken, friendlyFire, description, gang_value) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?) ON DUPLICATE KEY " +
                "UPDATE name = ?, created = ?, leader = ?, level = ?, coins = ?, bankBalance = ?, " +
                "kills = ?, deaths = ?, blocksbroken = ?, friendlyFire = ?, description = ?, gang_value = ?;";

        // OLD: "UPDATE " + GANGS_TABLE + " SET name=?, created=?, leader=?, level=?, coins=?, " +
        //                "bankBalance=?, kills=?, deaths=?, friendlyFire=? WHERE uuid=?;"
        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement(UPDATE_DATA)) {
            int i = 1;

            // Setting insert variables
            statement.setString(i++, (gang.getID() == null ? null : gang.getID().toString()));
            statement.setString(i++, gang.getName());
            statement.setLong(i++, gang.getCreated());
            statement.setString(i++, (gang.getLeader() == null ? null : gang.getLeader().toString()));
            statement.setInt(i++, gang.getLevel());
            statement.setInt(i++, gang.getCoins());
            statement.setDouble(i++, gang.getBankBalance());
            statement.setInt(i++, gang.getKills());
            statement.setInt(i++, gang.getDeaths());
            statement.setInt(i++, gang.getBlocksBroken());
            statement.setBoolean(i++, gang.hasFriendlyFire());
            statement.setString(i++, gang.getDescription());
            statement.setLong(i++, gang.getValue());

            // TODO: 14/04/2022 check if everything here is right and not missdone cz i fucked it up
            // Setting update variables
            statement.setString(i++, gang.getName());
            statement.setLong(i++, gang.getCreated());
            statement.setString(i++, (gang.getLeader() == null ? null : gang.getLeader().toString()));
            statement.setInt(i++, gang.getLevel());
            statement.setInt(i++, gang.getCoins());
            statement.setDouble(i++, gang.getBankBalance());
            statement.setInt(i++, gang.getKills());
            statement.setInt(i++, gang.getDeaths());
            statement.setInt(i++, gang.getBlocksBroken());
            statement.setBoolean(i++, gang.hasFriendlyFire());
            statement.setString(i++, gang.getDescription());
            statement.setLong(i, gang.getValue());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean doesGangDataExist(UUID uuid) {
        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return false;
        }

        // Not checking cache as we want to check whether the data exists in the database (?)

        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("SELECT name FROM `" + GANGS_TABLE + "` where uuid=?")) {
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            boolean exists = rs.next();

            rs.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean doesGangNameExist(String name) {
        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return false;
        }

        // Not checking cache as we want to check whether the data exists in the database (?)

        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("SELECT uuid FROM `" + GANGS_TABLE + "` where name=?")) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            boolean exists = rs.next();

            rs.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isGangDataLoaded(UUID uuid) {
        return gangs.containsKey(uuid);
    }

    @Override
    public void loadGangData(UUID uuid) {
        getGangData(uuid);
    }

    @Override
    public void unloadGangData(UUID uuid) {
        if (gangs.get(uuid) != null)
            updateGangData(gangs.remove(uuid));
    }

    @Override
    public String getGangName(UUID uuid) {
        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("SELECT name FROM `" + GANGS_TABLE + "` WHERE uuid=?")) {
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            String name = null;
            if (rs.next())
                name = rs.getString("name");

            rs.close();

            return name;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UUID getGangUniqueId(String name) {
        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("SELECT uuid FROM `" + GANGS_TABLE + "` WHERE name=?")) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            UUID uuid = null;

            if (rs.next())
                uuid = UUID.fromString(rs.getString("uuid"));

            rs.close();

            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ArrayList<GPlayer> getGangMembers(UUID gangId) {
        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("SELECT uuid FROM `" + PLAYERS_TABLE + "` WHERE gang=?")) {
            statement.setString(1, gangId.toString());
            ResultSet rs = statement.executeQuery();
            ArrayList<GPlayer> members = new ArrayList<>();
            GPlayer gPlayer;

            while (rs != null && rs.next()) {
                gPlayer = plugin.getGPlayerDataFactory().getGPlayerData(UUID.fromString(rs.getString("uuid")));
                members.add(gPlayer);
            }

            rs.close();

            return members;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<GPlayer>();
    }

    @Override
    public void updateLeaderboardTopValues() {
        int limit = plugin.getConfig().getInt("gang.leaderboard-data-pull-amount");
        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("SELECT * FROM `" + GANGS_TABLE + "` GROUP BY name ORDER BY gang_value DESC LIMIT "+limit+";")) {
            ResultSet rs = statement.executeQuery();
            LinkedHashMap<Integer, Gang> top_values = new LinkedHashMap<>();
            AtomicInteger i = new AtomicInteger(1);

            Gang gang;

            while (rs != null && rs.next()) {
                gang = getGangData(UUID.fromString(rs.getString("uuid")));
                top_values.put(i.get(), gang);
            }
            plugin.getLeaderboardManager().updateTopValues(top_values);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int startSavingTask() {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (Gang gang : gangs.values())
                updateGangData(gang);
        }, 20L * 60L * plugin.getConfig().getInt("gang.save-interval"), 20L * 60L * plugin.getConfig().getInt("gang.save-interval")).getTaskId();
    }

}