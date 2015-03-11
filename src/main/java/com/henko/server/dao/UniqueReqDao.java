package com.henko.server.dao;

import com.henko.server.model.UniqueReq;

import java.util.List;

public interface UniqueReqDao {
    public List<UniqueReq> getNUniqueReq(int amount);

    public int getNumOfUniqueReq();

    public int getNumOfAllReq();
}
