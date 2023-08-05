package net.withery.gangsx.datafactory.player.sql;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.datafactory.player.GPlayerDataFactory;
import net.withery.gangsx.datafactory.sql.SQLHandler;
import net.withery.gangsx.enums.Role;
import net.withery.gangsx.objects.GPlayer;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SQLGPlayerDataFactory extends GPlayerDataFactory {

    private final static Map<UUID, GPlayer> players = new HashMap<>();

    private final SQLHandler sqlHandler;
    private final String PLAYERS_TABLE;
    private int savingTask;

    public SQLGPlayerDataFactory(GangsX plugin, SQLHandler sqlHandler, String prefix) {
        super(plugin);
        this.sqlHandler = sqlHandler;
        this.PLAYERS_TABLE = prefix + "players";
    }

    @Override
    public boolean initialize() {
        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return false;
        }

        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + PLAYERS_TABLE + " " +
                "(uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(16), gang VARCHAR(36), role VARCHAR(16), kills INT, deaths INT, chatEnabled BOOLEAN);")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        savingTask = startSavingTask();
        return true;
    }

    @Override
    public void terminate() {
        Bukkit.getScheduler().cancelTask(savingTask);
        savingTask = 0;

        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return;
        }

        for (GPlayer gPlayer : players.values())
            updateGPlayerData(gPlayer);

        players.clear();
    }

    @Override
    public void initializeGPlayerData(GPlayer gPlayer) {
        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return;
        }

        if (doesGPlayerDataExist(gPlayer.getID())) return;
        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("INSERT INTO " + PLAYERS_TABLE + " " +
                "(uuid, name, gang, role, kills, deaths) VALUES (?, ?, ?, ?, ?, ?);")) {
            statement.setString(1, (gPlayer.getID() == null ? null : gPlayer.getID().toString()));
            statement.setString(2, gPlayer.getName());
            statement.setString(3, (gPlayer.getGangId() == null ? null : gPlayer.getGangId().toString()));
            statement.setString(4, (gPlayer.getRole() == null ? null : gPlayer.getRole().name()));
            statement.setInt(5, gPlayer.getKills());
            statement.setInt(6, gPlayer.getDeaths());
            statement.setBoolean(7, gPlayer.hasChatEnabled());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!players.containsKey(gPlayer.getID()))
            players.put(gPlayer.getID(), gPlayer);
    }

    @Override
    public void deleteGPlayerData(UUID uuid) {
        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return;
        }

        if (!doesGPlayerDataExist(uuid)) return;
        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("DELETE FROM " + PLAYERS_TABLE + " WHERE uuid=?")) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        players.remove(uuid);
    }

    @Override
    public GPlayer getGPlayerData(UUID uuid) {
        if (uuid == null) return null;

        if (players.get(uuid) != null)
            return players.get(uuid);

        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return null;
        }

        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("SELECT * FROM " + PLAYERS_TABLE + " WHERE uuid=?")) {
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();
            GPlayer gPlayer = null;

            if (rs.next()) {
                String stringUUID = rs.getString("uuid");
                UUID uuidDB = (stringUUID == null ? null : UUID.fromString(stringUUID));
                String name = rs.getString("name");
                String stringGangID = rs.getString("gang");
                UUID gangId = (stringGangID == null ? null : UUID.fromString(stringGangID));
                String stringRole = rs.getString("role");
                Role role = (stringRole == null ? null : Role.valueOf(stringRole));
                int kills = rs.getInt("kills");
                int deaths = rs.getInt("deaths");
                boolean chatEnabled = rs.getBoolean("chatEnabled");

                gPlayer = new GPlayer(plugin, uuidDB, name, gangId, role, kills, deaths, chatEnabled);

                if (!players.containsKey(gPlayer.getID()))
                    players.put(gPlayer.getID(), gPlayer);
            }

            rs.close();
            return gPlayer;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateGPlayerData(GPlayer gPlayer) {
        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return;
        }

        // Insert if not exists, update if exists
        final String UPDATE_DATA = "INSERT INTO `" + PLAYERS_TABLE + "` (uuid, name, gang, role, kills, deaths, chatEnabled) VALUES (?, ?, ?, ?, ?, ?,?) ON DUPLICATE KEY " +
                "UPDATE name = ?, gang = ?, role = ?, kills = ?, deaths = ?, chatEnabled = ?;";

        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement(UPDATE_DATA)) {
            int i = 1;

            // Setting insert variables
            statement.setString(i++, (gPlayer.getID() == null ? null : gPlayer.getID().toString()));
            statement.setString(i++, gPlayer.getName());
            statement.setString(i++, (gPlayer.getGangId() == null ? null : gPlayer.getGangId().toString()));
            statement.setString(i++, (gPlayer.getRole() == null ? null : gPlayer.getRole().name()));
            statement.setInt(i++, gPlayer.getKills());
            statement.setInt(i++, gPlayer.getDeaths());
            statement.setBoolean(i++, gPlayer.hasChatEnabled());

            // TODO: 14/04/2022 check if everything here is right and not missdone cz i fucked it up
            // Setting update variables
            statement.setString(i++, gPlayer.getName());
            statement.setString(i++, (gPlayer.getGangId() == null ? null : gPlayer.getGangId().toString()));
            statement.setString(i++, (gPlayer.getRole() == null ? null : gPlayer.getRole().name()));
            statement.setInt(i++, gPlayer.getKills());
            statement.setInt(i++, gPlayer.getDeaths());
            statement.setBoolean(i, gPlayer.hasChatEnabled());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean doesGPlayerDataExist(UUID uuid) {
        if (!sqlHandler.isConnected() && !sqlHandler.connect()) {
            plugin.getLogger().severe("Can't establish a database connection!");
            return false;
        }

        // Not checking cache as we want to check whether the data exists in the database (?)

        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("SELECT uuid FROM `" + PLAYERS_TABLE + "` where uuid=?")) {
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
    public boolean isGPlayerDataLoaded(UUID uuid) {
        return players.containsKey(uuid);
    }

    @Override
    public void loadGPlayerData(UUID uuid) {
        getGPlayerData(uuid);
    }

    @Override
    public void unloadGPlayerData(UUID uuid) {
        if (players.get(uuid) != null)
            updateGPlayerData(players.remove(uuid));
    }

    @Override
    public String getGPlayerName(UUID uuid) {
        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("SELECT name FROM `" + PLAYERS_TABLE + "` WHERE uuid=?")) {
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
    public UUID getGPlayerUniqueId(String name) {
        try (PreparedStatement statement = sqlHandler.getConnection().prepareStatement("SELECT uuid FROM `" + PLAYERS_TABLE + "` WHERE name=?")) {
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

    private int startSavingTask() {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (GPlayer gPlayer : players.values())
                updateGPlayerData(gPlayer);
        }, 20L * 60L * 5, 20L * 60L * 5).getTaskId(); // TODO: 15/04/2022 get config values for timer
    }

}