package com.henko.server.dao.impl;

import com.henko.server.dao.RedirectInfoDao;
import com.henko.server.db.connectionpool.HikariConnPool;
import com.henko.server.model.RedirectInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.henko.server.db.DBUtil.close;


public class H2RedirectInfoDao implements RedirectInfoDao{
    private HikariConnPool pool;

    public H2RedirectInfoDao() {
        pool = HikariConnPool.getConnPool();
    }

    @Override
    public RedirectInfo selectById(int id) {
        RedirectInfo info = null;
        String selectStr = "SELECT * FROM REDIRECTS WHERE ID_REDIRECT = ?;";

        Connection conn = pool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(selectStr);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();
            String url = null;
            int count = 0;
            while(rs.next()){
                url = rs.getString(2);
                count = rs.getInt(3);
            }

            if (redirectNotFound(url)) return null;

            info = new RedirectInfo(id, url, count);
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
    public RedirectInfo selectByUrl(String url) {
        RedirectInfo info = null;
        String selectStr = "SELECT * FROM REDIRECTS WHERE URL = ?;";

        Connection conn = pool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(selectStr);
            stmt.setString(1, url);

            rs = stmt.executeQuery();
            int id = 0;
            int count = 0;
            while(rs.next()){
                id = rs.getInt(1);
                count = rs.getInt(3);
            }

            if (redirectNotFound(id)) return null;

            info = new RedirectInfo(id, url, count);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

        return info;
    }

    private boolean redirectNotFound(String url) {
        return url == null;
    }

    private boolean redirectNotFound(int id) {
        return id == 0;
    }

    @Override
    public int persist(String url) {
        String insertStr = "INSERT INTO REDIRECTS (URL, R_COUNT) VALUES(?, 1);";

        Connection conn = pool.getConnection();
        PreparedStatement stmt = null;
        int persistId = 0;
        try {
            stmt = conn.prepareStatement(insertStr);
            stmt.setString(1, url);
            stmt.executeUpdate();

            RedirectInfo redirectInfo = selectByUrl(url);
            persistId = redirectInfo.getId();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }

        return persistId;
    }

    @Override
    public boolean updateCountByUrl(String url, int count) {
        if (!redirectExistInDB(url)) return false;

        String updateStr = "UPDATE REDIRECTS SET R_COUNT = ? WHERE URL = ?;";

        Connection conn = pool.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(updateStr);
            stmt.setInt(1, count);
            stmt.setString(2, url);
            stmt.executeUpdate();
            
            RedirectInfo result = selectByUrl(url);
            if (!countWasChanged(count, result)) return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }

        return true;
    }

    private boolean countWasChanged(int count, RedirectInfo result) {
        return result.getCount() == count;
    }

    private boolean redirectExistInDB(String url) {
        String queryStr = "SELECT URL FROM REDIRECTS WHERE URL = ?";

        Connection conn = pool.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(queryStr);
            stmt.setString(1, url);
            rs = stmt.executeQuery();
            while (rs.next()){
                if (redirectNotFound(rs.getString(1))) return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

        return true;
    }

    @Override
    public boolean increaseCountByUrl(String url) {
        RedirectInfo redirectInfo = selectByUrl(url);
        if (redirectInfo == null) return false;

        String updateStr = "UPDATE REDIRECTS SET R_COUNT = ? WHERE URL = ?;";
        int futureCount = redirectInfo.getCount() + 1;

        Connection conn = pool.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(updateStr);
            stmt.setInt(1, futureCount);
            stmt.setString(2, url);
            stmt.executeUpdate();

            RedirectInfo result = selectByUrl(url);
            if (!countWasChanged(futureCount, result)) return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(stmt);
            close(conn);
        }

        return true;
    }

    @Override
    public List<RedirectInfo> selectAll() {
        List<RedirectInfo> infoList = new ArrayList<>();
        String selectStr = "SELECT * FROM REDIRECTS;";

        Connection conn = pool.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();

            rs = stmt.executeQuery(selectStr);
            while(rs.next()){
                int id = rs.getInt(1);
                String url = rs.getString(2);
                int count = rs.getInt(3);

                infoList.add(new RedirectInfo(id, url, count));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

        if (isResultEmpty(infoList)) return null;
        return infoList;
    }

    private boolean isResultEmpty(List<RedirectInfo> infoList) {
        return infoList.size() == 0;
    }
}
