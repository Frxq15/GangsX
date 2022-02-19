package net.withery.gangsx.datafactory.sql;

import net.withery.gangsx.GangsX;

import java.sql.*;

public class SQLManager {
    private final GangsX plugin;
    private final String host, database, username, password;
    private final int port;
    private Connection connection;

    public SQLManager(GangsX plugin, String host, String database, String username, String password, int port) {
        this.plugin = plugin;
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }
    public synchronized boolean connect() {

        try {
            if(getConnection() != null && !getConnection().isClosed()) return false;
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password="
                    + password);
            plugin.log("Connected to mysql successfully.");
            return true;
        } catch (SQLException e) {
            connection = null;
            plugin.log("An error occured whilst connecting to mysql.");
            e.printStackTrace();
            return false;
        }
    }
    public synchronized Connection getConnection() {
        return connection;
    }
    public synchronized void closeConnection() {
        if(getConnection() == null) return;

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public synchronized void refreshConnection() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1");
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            connect();
        }
    }
    public synchronized boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

}
