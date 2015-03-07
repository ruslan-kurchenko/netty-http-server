package com.henko.server.dao.impl;

import com.henko.server.dao.ConnectionInfoDao;
import com.henko.server.dao.connectionpool.HikariConnPool;
import com.henko.server.domain.UniqueRequestInfo;
import com.henko.server.model.ConnectionInfo;

import java.sql.Connection;
import java.util.List;

public class H2ConnectionInfoDao implements ConnectionInfoDao{
    HikariConnPool pool;

    public H2ConnectionInfoDao() {
        pool = HikariConnPool.getConnPool();
    }

    @Override
    public ConnectionInfo selectById() {
        return null;
    }

    @Override
    public List<ConnectionInfo> selectAll() {
        return null;
    }

    @Override
    public List<UniqueRequestInfo> selectUniqueRequestInfo() {
        return null;
    }

    @Override
    public int insertConnectionInfo() {
        return 0;
    }

    @Override
    public boolean deleteConnectionInfo() {
        return false;
    }

    @Override
    public int selectNumberOfUniqueRequest() {
        return 0;
    }

    @Override
    public int selectNumberOfCurrentConn() {
        return 0;
    }

    @Override
    public int selectNumberOfAllRequests() {
        return 0;
    }
}
