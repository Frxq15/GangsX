package me.frxq.gangsx.datafactory.sql;

import me.frxq.gangsx.GangsX;

import java.sql.*;

public class SQLHandler {
    private final GangsX plugin;
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private Connection connection;

    public SQLHandler(GangsX plugin, String host, String database, String username, String password, int port) {
        this.plugin = plugin;
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public synchronized boolean connect() {
        try {
            if (connection != null && !connection.isClosed()) return true;

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            connection = null;
            e.printStackTrace();
            return false;
        }
    }

    public synchronized void disconnect() {
        if (connection == null) return;

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public synchronized Connection getConnection() {
        return connection;
    }

}
