package com.henko.server.dao;

import com.henko.server.model.UniqueReq;
import com.henko.server.model.Connect;

import java.util.List;

public interface ConnectDao {

    public Connect getById(int id);

    public List<Connect> getAll();

    public List<Connect> getLastNConn(int amount);

    public int getNumOfCurrentConn();

    public void insertConnect(Connect connect);

    public int getNumOfAllConn();
}
