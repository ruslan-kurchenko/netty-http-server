package com.henko.server.dao.impl;

import com.henko.server.dao.ConnectionInfoDao;
import com.henko.server.db.connectionpool.HikariConnPool;
import com.henko.server.domain.UniqueRequestInfo;
import com.henko.server.handler.NumberConnectionHandler;
import com.henko.server.model.ConnectionInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.henko.server.db.DBUtil.close;

public class H2ConnectionInfoDao implements ConnectionInfoDao{
    HikariConnPool pool;

    public H2ConnectionInfoDao() {
        pool = HikariConnPool.getConnPool();
    }

    @Override
    public ConnectionInfo selectById(int id) {
        String selectStr = "SELECT * FROM CONNECTIONS WHERE ID_CONNECTION = ?";

        Connection conn = pool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(selectStr);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (isEmpty(rs)) return null;

            return parseConnectionInfo(id, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

        return null;
    }

    @Override
    public ConnectionInfo selectByTimeStamp(long timeStamp) {
        String selectStr = "SELECT * FROM CONNECTIONS WHERE TIME_STAMP = ?";

        Connection conn = pool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(selectStr);
            stmt.setLong(1, timeStamp);
            rs = stmt.executeQuery();

            if (isEmpty(rs)) return null;

            return parseConnectionInfo(timeStamp, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

        return null;
    }

    @Override
    public List<ConnectionInfo> selectAll() {
        String selectStr = "SELECT * FROM CONNECTIONS;";

        Connection conn = pool.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectStr);

            if (isEmpty(rs)) return null;

            return parseConnectionInfoList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

        return null;
    }

    @Override
    public List<UniqueRequestInfo> selectUniqueRequestInfo() {
        String selectStr = "SELECT SRC_IP, COUNT(SRC_IP), MAX(TIME_STAMP) FROM CONNECTIONS GROUP BY SRC_IP;";

        Connection conn = pool.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectStr);

            if (isEmpty(rs)) return null;

            return parseUniqueRequestInfoList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

        return null;
    }

    @Override
    public void insertConnectionInfo(ConnectionInfo connInfo) {
        String insertStr = "INSERT INTO " +
                "CONNECTIONS(SRC_IP, URI, TIME_STAMP, SEND_B, RECEIVED_B, SPEED) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        Connection conn = pool.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(insertStr);
            stmt.setString(1, connInfo.getIp());
            stmt.setString(2, connInfo.getUri());
            stmt.setLong(3, connInfo.getTimestamp());
            stmt.setLong(4, connInfo.getSendBytes());
            stmt.setLong(5, connInfo.getReceivedBytes());
            stmt.setLong(6, connInfo.getSpeed());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
    }

    @Override
    public int selectNumberOfUniqueRequest() {
        String selectStr = "SELECT COUNT(DISTINCT SRC_IP) FROM CONNECTIONS;";

        Connection conn = pool.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectStr);

            if (isEmpty(rs)) return 0;

            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

        return 0;
    }

    @Override
    public int selectNumberOfCurrentConn() {
        return NumberConnectionHandler.getConnectionCount();
    }

    @Override
    public int selectNumberOfAllRequests() {
        String selectStr = "SELECT COUNT(SRC_IP) FROM CONNECTIONS;";

        Connection conn = pool.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectStr);

            if (isEmpty(rs)) return 0;

            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

        return 0;
    }

    @Override
    public List<ConnectionInfo> selectLast16ConnInfo() {
//        String selectStr = "" +
//                "SELECT * " +
//                "FROM CONNECTIONS " +
//                "WHERE ID_CONNECTION IN " +
//                "                       ( " +
//                "                          SELECT TOP(16) ID_CONNECTION " +
//                "                          FROM CONNECTIONS " +
//                "                          ORDER BY ID_CONNECTION DESC" +
//                "                       ) " +
//                "ORDER BY ID_CONNECTION ASC;";

//        String selectStr = "SELECT * \n" +
//                "FROM (" +
//                "       SELECT * " +
//                "       FROM CONNECTIONS " +
//                "       ORDER BY ID_CONNECTION DESC LIMIT 3" +
//                "     ) AS tbl " +
//                "ORDER BY tbl.ID_CONNECTION ASC;";
//
//
        String selectStr = "SELECT * FROM CONNECTIONS LIMIT 16;";

        Connection conn = pool.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectStr);

            if (isEmpty(rs)) return null;

            return parseConnectionInfoList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

        return null;
    }

    /**************************************
     * Inner helper methods:
     *************************************/

    private boolean isEmpty(ResultSet rs) throws SQLException {
        return !rs.next();
    }

    private ConnectionInfo parseConnectionInfo(int id, ResultSet rs) throws SQLException {
        String ip = rs.getString(2);
        String uri = rs.getString(3);
        long timestamp = rs.getLong(4);
        long sendBytes = rs.getLong(5);
        long receivedBytes = rs.getLong(6);
        long speed = rs.getLong(7);

        return new ConnectionInfo(id, ip, uri, timestamp, sendBytes, receivedBytes, speed);
    }

    private ConnectionInfo parseConnectionInfo(long timeStamp, ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String ip = rs.getString(2);
        String uri = rs.getString(3);
        long sendBytes = rs.getLong(5);
        long receivedBytes = rs.getLong(6);
        long speed = rs.getLong(7);

        return new ConnectionInfo(id, ip, uri, timeStamp, sendBytes, receivedBytes, speed);
    }

    private List<ConnectionInfo> parseConnectionInfoList(ResultSet rs) throws SQLException {
        List<ConnectionInfo> infoList = new ArrayList<>();
        do {
            int id = rs.getInt(1);
            String ip = rs.getString(2);
            String uri = rs.getString(3);
            long timestamp = rs.getLong(4);
            long sendBytes = rs.getLong(5);
            long receivedBytes = rs.getLong(6);
            long speed = rs.getLong(7);

            infoList.add(new ConnectionInfo(id, ip, uri, timestamp, sendBytes, receivedBytes, speed));
        } while (rs.next());

        return infoList;
    }

    private List<UniqueRequestInfo> parseUniqueRequestInfoList(ResultSet rs) throws SQLException {
        List<UniqueRequestInfo> requestInfoList = new ArrayList<>();
        do {
            String ip = rs.getString(1);
            int count = rs.getInt(2);
            long lastConn = rs.getLong(3);

            requestInfoList.add(new UniqueRequestInfo(ip, count, new Date(lastConn)));
        } while (rs.next());

        return requestInfoList;
    }
}
