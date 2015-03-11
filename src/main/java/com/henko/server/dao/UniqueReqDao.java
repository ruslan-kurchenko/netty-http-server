package com.henko.server.dao;

import com.henko.server.model.UniqueReq;

import java.util.List;

public interface UniqueReqDao {

    public UniqueReq getByIp(String url);

    public void addOrIncrementCount(UniqueReq request);

    public List<UniqueReq> getNUniqueReq(int amount);

    public int getNumOfUniqueConn();

    public int getNumOfAllConn();
}
