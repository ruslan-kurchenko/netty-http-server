package com.henko.server.dao.impl;

import com.henko.server.dao.UniqueReqDao;
import com.henko.server.db.HikariConnPool;
import com.henko.server.model.UniqueReq;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.henko.server.db.DBUtil.close;

public class H2UniqueReqDao implements UniqueReqDao {
    private HikariConnPool _pool;

    public H2UniqueReqDao() {
        _pool = HikariConnPool.getConnPool();
    }

    @Override
    public UniqueReq getByIp(String ip) {
        String selectStr = "SELECT * FROM UNIQUE_REQUESTS WHERE IP = ?;";

        Connection conn = _pool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(selectStr);
            stmt.setString(1, ip);
            rs = stmt.executeQuery();

            if (_isEmpty(rs)) return null;

            int id = rs.getInt("ID");
            int count = rs.getInt("COUNT");
            long lastConn = rs.getLong("LAST_CONN");

            return new UniqueReq(id, ip, count, lastConn);
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
    public List<UniqueReq> getNUniqueReq(int amount) {
        String selectStr = "" +
                "SELECT * " +
                "FROM (" +
                "       SELECT *" +
                "       FROM UNIQUE_REQUESTS" +
                "       ORDER BY COUNT DESC LIMIT ?" +
                "     ); ";

        Connection conn = _pool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(selectStr);
            stmt.setInt(1, amount);
            rs = stmt.executeQuery();

            if (_isEmpty(rs)) return null;

            return _parseRedirectInfoList(rs);
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
    public int getNumOfUniqueConn() {
        String selectStr = "SELECT COUNT(DISTINCT IP) FROM UNIQUE_REQUESTS;";

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
    public int getNumOfAllConn() {
        String selectStr = "SELECT SUM(COUNT) FROM UNIQUE_REQUESTS;";

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
    public void addOrIncrementCount(UniqueReq request) {
        UniqueReq present  = getByIp(request.getIp());

        if (present == null) {
            _insertRequest(request);
        } else {
            _incrementCount(present);
        }
    }


    private void _incrementCount(UniqueReq request) {
        String updateStr = "UPDATE UNIQUE_REQUESTS SET COUNT = ? WHERE IP = ?;";

        Connection conn = _pool.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(updateStr);
            stmt.setInt(1, request.getCount() + 1);
            stmt.setString(2, request.getIp());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }
    }

    private int _insertRequest(UniqueReq request) {
        String insertStr = "INSERT INTO UNIQUE_REQUESTS (IP, COUNT, LAST_CONN) VALUES(?, ?, ?);";

        Connection conn = _pool.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(insertStr);
            stmt.setString(1, request.getIp());
            stmt.setInt(2, 1);
            stmt.setLong(3, request.getLastConn());
            stmt.executeUpdate();

            return getByIp(request.getIp()).getId();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }

        return 0;
    }

    private List<UniqueReq> _parseRedirectInfoList(ResultSet rs) throws SQLException {
        List<UniqueReq> requests = new ArrayList<>();
        do {
            int id = rs.getInt("ID");
            String ip = rs.getString("IP");
            int count = rs.getInt("COUNT");
            long lastConn = rs.getLong("LAST_CONN");

            requests.add(new UniqueReq(id, ip, count, lastConn));
        } while (rs.next());

        return requests;
    }

    private boolean _isEmpty(ResultSet rs) throws SQLException {
        return !rs.next();
    }
}
