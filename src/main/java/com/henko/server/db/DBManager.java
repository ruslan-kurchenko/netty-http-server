package com.henko.server.db;

import java.sql.*;

import static com.henko.server.db.DBUtil.*;

public class DBManager {

    private HikariConnPool _pool;

    public DBManager() {
        _pool = HikariConnPool.getConnPool();
    }

    public void initialiseDB() {
        Connection conn = _pool.getConnection();
        try {
            _createConnectionInfoTable(conn);
            _createRedirectInfoTable(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
    }

    public void dropTables() {
        String dropRedirectsStr = "DROP TABLE IF EXISTS REDIRECTS;";
        String dropConnectionsStr = "DROP TABLE IF EXISTS CONNECTIONS;";

        Connection conn = _pool.getConnection();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(dropConnectionsStr);
            stmt.executeUpdate(dropRedirectsStr);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
            close(stmt);
        }
    }

    public void cleanConnectionInfoTable(int leftRows) {
        String selectMaxStr = "SELECT COUNT(ID_CONNECTION) FROM CONNECTIONS";
        String deleteStr = "" +
                "DELETE FROM CONNECTIONS " +
                "WHERE ID_CONNECTION IN (" +
                "   SELECT ID_CONNECTION " +
                "   FROM CONNECTIONS " +
                "   LIMIT ?);";

        Connection conn = _pool.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectMaxStr);
            int numOfRows = _parseNumOfRows(rs);

            if (_tableBalanced(numOfRows, leftRows)) return;

            int numRowsToDelete = numOfRows - leftRows;

            _doClean(deleteStr, conn, numRowsToDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
    }

    private void _doClean(String deleteStr, Connection conn, int numRowsToDelete) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(deleteStr);
        ps.setInt(1, numRowsToDelete);
        ps.executeUpdate();

        close(ps);
    }

    private boolean _tableBalanced(int numOfRows, int leftRows) {
        return numOfRows <= leftRows;
    }

    private int _parseNumOfRows(ResultSet rs) throws SQLException {
        rs.next();
        return rs.getInt(1);
    }

    private void _createConnectionInfoTable(Connection conn) throws SQLException {
        String createTableSQL = "" +
                "CREATE TABLE IF NOT EXISTS CONNECTIONS( " +
                "ID_CONNECTION INT PRIMARY KEY AUTO_INCREMENT, " +
                "SRC_IP VARCHAR(45) NOT NULL, " +
                "URI VARCHAR(45) NOT NULL, " +
                "TIME_STAMP DECIMAL NOT NULL, " +
                "SEND_B DECIMAL NOT NULL, " +
                "RECEIVED_B DECIMAL NOT NULL, " +
                "SPEED DECIMAL NOT NULL);";

        _createTable(conn, createTableSQL);
    }

    private void _createRedirectInfoTable(Connection conn) throws SQLException {
        String createTableSQL = "" +
                "CREATE TABLE IF NOT EXISTS REDIRECTS(" +
                "ID_REDIRECT INT PRIMARY KEY AUTO_INCREMENT, " +
                "URL VARCHAR(45) NOT NULL, " +
                "R_COUNT INT NOT NULL);";

        _createTable(conn, createTableSQL);
    }

    private void _createTable(Connection conn, String sqlQuery) throws SQLException {
        conn.createStatement().executeUpdate(sqlQuery);
    }

}
