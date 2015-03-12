package com.henko.server.dao.impl;

import com.henko.server.dao.ConnectDao;
import com.henko.server.db.HikariConnPool;
import com.henko.server.domain.UniqueRequest;
import com.henko.server.handler.ServerConnectionCountHandler;
import com.henko.server.model.Connect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.henko.server.db.DBUtil.close;

public class H2ConnectDao implements ConnectDao {
    private HikariConnPool _pool;

    private final static AtomicInteger LAST_INSERTED_ID = new AtomicInteger();

    public H2ConnectDao() {
        _pool = HikariConnPool.getConnPool();
    }

    @Override
    public Connect getById(int id) {
        String selectStr = "SELECT * FROM CONNECTIONS WHERE ID_CONNECTION = ?";

        java.sql.Connection conn = _pool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(selectStr);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (_isEmpty(rs)) return null;

            return _parseConnect(id, rs);
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
    public List<Connect> getAll() {
        String selectStr = "SELECT * FROM CONNECTIONS;";

        Connection conn = _pool.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectStr);

            if (_isEmpty(rs)) return null;

            return _parseConnectionList(rs);
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
    public List<Connect> getLastNConn(int amount) {
        if (LAST_INSERTED_ID.get() == 0) return null;

        int last = LAST_INSERTED_ID.get();
        int first = 0;

        if (!(LAST_INSERTED_ID.get() < amount)) first = LAST_INSERTED_ID.get() - amount;

        String selectStr = "SELECT * FROM CONNECTIONS WHERE ID_CONNECTION  BETWEEN ? AND ? ORDER BY ID_CONNECTION;";

        Connection conn = _pool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(selectStr);
            stmt.setInt(1, first);
            stmt.setInt(2, last);
            rs = stmt.executeQuery();

            if (_isEmpty(rs)) return null;

            return _parseConnectionList(rs);
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
    public List<UniqueRequest> getNUniqueRequest(int amount) {
        String selectStr = "SELECT SRC_IP, COUNT(SRC_IP), MAX(TIME_STAMP) FROM CONNECTIONS GROUP BY SRC_IP LIMIT ?;";

        Connection conn = _pool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(selectStr);
            stmt.setInt(1, amount);
            rs = stmt.executeQuery();

            if (_isEmpty(rs)) return null;

            return _parseUniqueRequestList(rs);
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
    public int getNumOfAllConn() {
        return ServerConnectionCountHandler.getAllConnCount();
    }

    @Override
    public int getNumOfUniqueConn() {
        String selectStr = "SELECT COUNT(DISTINCT SRC_IP) FROM CONNECTIONS;";

        Connection conn = _pool.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectStr);

            if (_isEmpty(rs)) return 0;

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
    public int getNumOfCurrentConn() {
        return ServerConnectionCountHandler.getConnectionCount();
    }

    @Override
    public void insertConnect(Connect connect) {
        String insertStr = "INSERT INTO " +
                "CONNECTIONS(SRC_IP, URI, TIME_STAMP, SEND_B, RECEIVED_B, SPEED) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        Connection conn = _pool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(insertStr, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, connect.getIp());
            stmt.setString(2, connect.getUri());
            stmt.setLong(3, connect.getTimestamp());
            stmt.setLong(4, connect.getSendBytes());
            stmt.setLong(5, connect.getReceivedBytes());
            stmt.setLong(6, connect.getSpeed());
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();

            if (_isEmpty(rs)) return;

            LAST_INSERTED_ID.set(rs.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }
    }

    /**************************************
     * Inner helper methods:
     *************************************/

    private boolean _isEmpty(ResultSet rs) throws SQLException {
        return !rs.next();
    }

    private Connect _parseConnect(int id, ResultSet rs) throws SQLException {
        String ip = rs.getString(2);
        String uri = rs.getString(3);
        long timestamp = rs.getLong(4);
        long sendBytes = rs.getLong(5);
        long receivedBytes = rs.getLong(6);
        long speed = rs.getLong(7);

        return new Connect(id, ip, uri, timestamp, sendBytes, receivedBytes, speed);
    }

    private List<Connect> _parseConnectionList(ResultSet rs) throws SQLException {
        List<Connect> infoList = new ArrayList<>();
        do {
            int id = rs.getInt(1);
            String ip = rs.getString(2);
            String uri = rs.getString(3);
            long timestamp = rs.getLong(4);
            long sendBytes = rs.getLong(5);
            long receivedBytes = rs.getLong(6);
            long speed = rs.getLong(7);

            infoList.add(new Connect(id, ip, uri, timestamp, sendBytes, receivedBytes, speed));
        } while (rs.next());

        return infoList;
    }

    private List<UniqueRequest> _parseUniqueRequestList(ResultSet rs) throws SQLException {
        List<UniqueRequest> requestInfoList = new ArrayList<>();
        do {
            String ip = rs.getString(1);
            int count = rs.getInt(2);
            long lastConn = rs.getLong(3);

            requestInfoList.add(new UniqueRequest(ip, count, lastConn));
        } while (rs.next());

        return requestInfoList;
    }
}
