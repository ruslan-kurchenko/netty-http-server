package com.henko.server.dao;

import com.henko.server.model.UniqueReq;
import com.henko.server.model.Connect;

import java.util.List;

public interface ConnectDao {

    public Connect getById(int id);

    public List<Connect> getAll();

    public List<Connect> getLastNConn(int amount);

    public List<UniqueReq> getNUniqueRequest(int amount);

    public int getNumOfUniqueConn();

    public int getNumOfCurrentConn();

    public int getNumOfAllConn();

    public void insertConnect(Connect connect);
}
