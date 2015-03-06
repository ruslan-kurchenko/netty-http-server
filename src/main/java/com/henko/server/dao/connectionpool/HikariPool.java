package com.henko.server.dao.connectionpool;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariPool implements ConnectionPool{
    private static HikariPool instance = null;
    private HikariDataSource dataSource = null;
    
    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (HikariPool.class) {
                if (instance == null) {
                    instance = new HikariPool();
                }
            }
        }
        
        return instance;
    }
}
