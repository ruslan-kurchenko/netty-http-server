package com.henko.server.dao;

import com.henko.server.domain.UniqueRequest;
import com.henko.server.model.Connect;

import java.util.List;

public interface ConnectDao {

    public Connect getById(int id);

    public List<Connect> getAll();

    public List<Connect> getLastNConn(int amount);

    public List<UniqueRequest> getNUniqueRequest(int amount);

    public int getNumOfUniqueRequest();

    public int getNumOfCurrentConn();

    public int getNumOfAllRequests();

    public void insertConnectionInfo(Connect connect);
}
