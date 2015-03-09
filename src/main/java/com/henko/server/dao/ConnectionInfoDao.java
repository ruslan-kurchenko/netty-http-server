package com.henko.server.dao;

import com.henko.server.domain.UniqueRequestInfo;
import com.henko.server.model.ConnectionInfo;

import java.util.List;

public interface ConnectionInfoDao {

    public ConnectionInfo selectById(int id);

    public ConnectionInfo selectByTimeStamp(long timeStamp);

    public List<ConnectionInfo> selectAll();

    public List<ConnectionInfo> selectLast16ConnInfo();

    public List<UniqueRequestInfo> selectUniqueRequestInfo();

    public void insertConnectionInfo(ConnectionInfo connectionInfo);


    public int selectNumberOfUniqueRequest();

    public int selectNumberOfCurrentConn();

    public int selectNumberOfAllRequests();
}
