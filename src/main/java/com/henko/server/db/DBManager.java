package com.henko.server.db;

import com.henko.server.db.connectionpool.HikariConnPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.henko.server.db.DBUtil.*;

public class DBManager {

    private HikariConnPool pool;

    public DBManager() {
        pool = HikariConnPool.getConnPool();
    }

    public void initialiseDB() {
        createConnectionInfoTable();
        createRedirectInfoTable();
    }

    public void dropTables() {
        String dropRedirectsStr = "DROP TABLE IF EXISTS REDIRECTS;";
        String dropConnectionsStr = "DROP TABLE IF EXISTS CONNECTIONS;";

        try {
            Statement stmt = pool.getConnection().createStatement();
            stmt.executeUpdate(dropConnectionsStr);
            stmt.executeUpdate(dropRedirectsStr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createConnectionInfoTable() {
        String createTableSQL = "" +
                "CREATE TABLE IF NOT EXISTS CONNECTIONS( " +
                "ID_CONNECTION INT PRIMARY KEY AUTO_INCREMENT, " +
                "SRC_IP VARCHAR(45) NOT NULL, " +
                "URI VARCHAR(45) NOT NULL, " +
                "TIME_STAMP DECIMAL NOT NULL, " +
                "SEND_B DECIMAL NOT NULL, " +
                "RECEIVED_B DECIMAL NOT NULL, " +
                "SPEED DECIMAL NOT NULL);";

        createTable(createTableSQL);
    }

    private void createRedirectInfoTable() {
        String createTableSQL = "" +
                "CREATE TABLE IF NOT EXISTS REDIRECTS(" +
                "ID_REDIRECT INT PRIMARY KEY AUTO_INCREMENT, " +
                "URL VARCHAR(45) NOT NULL, " +
                "R_COUNT INT NOT NULL);";

        createTable(createTableSQL);
    }

    private void createTable(String sqlQuery) {
        Connection conn = pool.getConnection();
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
    }

}
