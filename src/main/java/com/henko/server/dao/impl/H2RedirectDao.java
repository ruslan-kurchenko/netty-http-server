package com.henko.server.dao.impl;

import com.henko.server.dao.RedirectDao;
import com.henko.server.dao.exception.PersistException;
import com.henko.server.db.HikariConnPool;
import com.henko.server.model.Redirect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.henko.server.db.DBUtil.close;


public class H2RedirectDao implements RedirectDao {
    private HikariConnPool _pool;

    public H2RedirectDao() {
        _pool = HikariConnPool.getConnPool();
    }

    @Override
    public Redirect getByUrl(String url) {
        Redirect info = null;
        String selectStr = "SELECT * FROM REDIRECTS WHERE URL = ?;";

        Connection conn = _pool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(selectStr);
            stmt.setString(1, url);

            rs = stmt.executeQuery();
            int id = 0;
            int count = 0;
            while (rs.next()) {
                id = rs.getInt(1);
                count = rs.getInt(3);
            }

            if (_redirectNotFound(id)) return null;

            info = new Redirect(id, url, count);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

        return info;
    }

    @Override
    public List<Redirect> getAll() {
        String selectStr = "SELECT * FROM REDIRECTS;";

        Connection conn = _pool.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectStr);

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
    public List<Redirect> getNRedirect(int amount) {
        String selectStr = "" +
                "SELECT * " +
                "FROM (" +
                "       SELECT *" +
                "       FROM REDIRECTS" +
                "       ORDER BY R_COUNT DESC LIMIT ?" +
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
    public void addOrIncrementCount(String url) throws PersistException {
        if (getByUrl(url) == null) {
            if (_insertRedirectInfo(url) == 0) throw new PersistException();
            return;
        }

        if (!_increaseCountByUrl(url)) throw new PersistException();
    }

    private int _insertRedirectInfo(String url) {
        String insertStr = "INSERT INTO REDIRECTS (URL, R_COUNT) VALUES(?, 1);";

        Connection conn = _pool.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(insertStr);
            stmt.setString(1, url);
            stmt.executeUpdate();

            Redirect redirect = getByUrl(url);
            return redirect.getId();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }

        return 0;
    }

    private boolean _increaseCountByUrl(String url) {
        Redirect redirect = getByUrl(url);

        String updateStr = "UPDATE REDIRECTS SET R_COUNT = ? WHERE URL = ?;";
        int futureCount = redirect.getCount() + 1;

        Connection conn = _pool.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(updateStr);
            stmt.setInt(1, futureCount);
            stmt.setString(2, url);
            stmt.executeUpdate();

            Redirect result = getByUrl(url);
            if (!_countChanged(futureCount, result)) return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }

        return true;
    }

    private List<Redirect> _parseRedirectInfoList(ResultSet rs) throws SQLException {
        List<Redirect> infoList = new ArrayList<>();
        do {
            int id = rs.getInt(1);
            String url = rs.getString(2);
            int count = rs.getInt(3);

            infoList.add(new Redirect(id, url, count));
        } while (rs.next());

        return infoList;
    }

    private boolean _redirectNotFound(int id) {
        return id == 0;
    }

    private boolean _isEmpty(ResultSet rs) throws SQLException {
        return !rs.next();
    }

    private boolean _countChanged(int count, Redirect result) {
        return result.getCount() == count;
    }
}
