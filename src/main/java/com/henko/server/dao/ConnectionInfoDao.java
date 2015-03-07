package com.henko.server.dao;

import com.henko.server.domain.UniqueRequestInfo;
import com.henko.server.model.ConnectionInfo;

import java.util.List;

public interface ConnectionInfoDao {

    public ConnectionInfo selectById(int id);

    public ConnectionInfo selectByTimeStamp(long timeStamp);

    public List<ConnectionInfo> selectAll();

    public List<UniqueRequestInfo> selectUniqueRequestInfo();

    public int insertConnectionInfo(ConnectionInfo connectionInfo);

    public boolean deleteConnectionInfo();

    public int selectNumberOfUniqueRequest();

    public int selectNumberOfCurrentConn();

    public int selectNumberOfAllRequests();
}
