package com.henko.server.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnPool {
    private static HikariDataSource dataSource;
    private static HikariConnPool connPool;

    private final static String CONFIG_PATH = "./config/db-config.properties";

    static {
        connPool = new HikariConnPool();
    }

    private HikariConnPool() {
        HikariConfig config = new HikariConfig(CONFIG_PATH);
        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection(){
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static HikariConnPool getConnPool() {
        return connPool;
    }
}
