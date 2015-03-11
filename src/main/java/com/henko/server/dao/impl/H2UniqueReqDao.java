package com.henko.server.dao.impl;

import com.henko.server.dao.UniqueReqDao;
import com.henko.server.db.HikariConnPool;
import com.henko.server.handler.ServerConnectionCountHandler;
import com.henko.server.model.UniqueReq;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.henko.server.db.DBUtil.close;

public class H2UniqueReqDao implements UniqueReqDao {

    @Override
    public int getNumOfAllReq() {
        return ServerConnectionCountHandler.getCountOfAllReq();
    }

    @Override
    public List<UniqueReq> getNUniqueReq(int amount) {
        return ServerConnectionCountHandler.getNUniqueReq();
    }

    @Override
    public int getNumOfUniqueReq() {
        return ServerConnectionCountHandler.getNumOfUniqueReq();
    }


}
