package net.withery.gangsx.datafactory.gang.sql;

import net.withery.gangsx.GangsX;
import net.withery.gangsx.Objects.Gang;
import net.withery.gangsx.datafactory.gang.GangDataFactory;
import net.withery.gangsx.datafactory.sql.SQLManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLGangDataFactory extends GangDataFactory {
    private final SQLManager sqlManager;
    private String table = "gangsx_gangs";

    public SQLGangDataFactory(GangsX plugin, SQLManager sqlManager) {
        super(plugin);
        this.sqlManager = sqlManager;
    }

    @Override
    public boolean initialize() {
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS "+table+" " +
                    "(uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(16), created BIGINT, leader VARCHAR(36), level INT, " +
                    "coins INT, bankBalance DOUBLE, kills INT, deaths INT, friendlyFire BOOLEAN);");
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void terminate() {

    }

    @Override
    public void initializeGangData(Gang gang) {
        if(doesGangDataExist(gang.getID())) return;
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement("INSERT INTO "+table+" " +
                    "(uuid,name,created,leader,level,coins,bankBalance,kills,deaths,friendlyFire) VALUES (?,?,?,?,?,?,?,?,?,?)");
            statement.setString(1, gang.getID().toString());
            statement.setString(2, gang.getName());
            statement.setLong(3, gang.getCreated());
            statement.setString(4, gang.getLeader().toString());
            statement.setInt(5, gang.getLevel());
            statement.setInt(6, gang.getCoins());
            statement.setDouble(7, gang.getBankBalance());
            statement.setInt(8, gang.getKills());
            statement.setInt(9, gang.getDeaths());
            statement.setBoolean(10, gang.hasFriendlyFire());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteGangData(UUID uuid) {
        if(!doesGangDataExist(uuid)) return;
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement("DELETE * from "+table+" WHERE uuid="+uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Gang getGangData(UUID uuid) {
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement("SELECT * from "+table+" WHERE uuid="+uuid);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                Gang gang = new Gang(GangsX.getInstance(), UUID.fromString(rs.getString("uuid")), rs.getString("name"),
                        rs.getLong("created"), UUID.fromString(rs.getString("leader")), rs.getInt("level"),
                        rs.getInt("coins"), rs.getDouble("bankBalance"), rs.getInt("kills"), rs.getInt("deaths"),
                        rs.getBoolean("friendlyFire"), null, null, null, null);
                return gang;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateGangData(Gang gang) {

    }

    @Override
    public boolean doesGangDataExist(UUID uuid) {
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement("SELECT gang FROM "+table+" where UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isGangDataLoaded(UUID uuid) {
        return false;
    }

    @Override
    public void loadGangData(UUID uuid) {

    }

    @Override
    public void unloadGangData(UUID uuid) {

    }

    @Override
    public String getGangName(UUID uuid) {
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement("SELECT name from "+table+" WHERE uuid="+uuid);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            return rs.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UUID getGangUniqueId(String name) {
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement("SELECT uuid from "+table+" WHERE name="+name);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            return UUID.fromString(rs.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}