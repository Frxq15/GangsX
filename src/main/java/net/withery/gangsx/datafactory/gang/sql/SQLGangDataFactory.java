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

    /*
    Hey Dan, this is the class you wanted to work on.
    You basically just need to fill in all the methods with your code that makes it do whatever it's supposed to do.
    As this is the SQLGangDataFactory, you could guess it's all about making this work with an SQL Database (and only
                                        covering the gang objects, the players are going to be handled independently).

    You can look at what the methods are supposed to do, by looking at the methods tooltips or reading the java docs I
    wrote in the GangDataFactory class (which this file is extending).

    It would be smart to add some sort of Map in this class which stores the UUID -> Gang (<UUID, Gang>), which would
    kind of be used as a cache (loadGangData, unloadGangData and isGangDataLoaded are methods that are related to that;
    getGangData should pull from the cache, if it contains the gang, instead of querying).

    There is no need to worry about making anything asynchronous as this is covered by the parent class (Methods such
    as initializeGangDataAsync which call the normal methods asynchronously and return the result as CompletableFuture).

    If you have any questions feel free to message me on Discord (kleinesNugget#5354 🙂)

    Yours sincerely
    Sven De riches
    your cute x
     */

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

    }

    @Override
    public Gang getGangData(UUID uuid) {
        try {
            PreparedStatement statement = sqlManager.getConnection().prepareStatement("SELECT * from "+table+" WHERE uuid=?"+uuid);

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
        return null;
    }

    @Override
    public UUID getGangUniqueId(String name) {
        return null;
    }

}