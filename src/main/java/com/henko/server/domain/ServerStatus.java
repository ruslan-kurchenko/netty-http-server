package com.henko.server.domain;

import com.henko.server.model.Connect;
import com.henko.server.model.Redirect;

import java.util.List;

public class ServerStatus {
    private int numOfAllRequests;
    private int numOfUniqueRequests;
    private int numOfCurrentConn;
    private List<UniqueRequest> uniqueRequestList;
    private List<Connect> connectList;
    private List<Redirect> redirectList;

    public ServerStatus() {
    }

    public ServerStatus(int numOfAllRequests,
                        int numOfUniqueRequests,
                        int numOfCurrentConn,
                        List<UniqueRequest> uniqueRequestList,
                        List<Connect> connectList,
                        List<Redirect> redirectList) {
        this.numOfAllRequests = numOfAllRequests;
        this.numOfUniqueRequests = numOfUniqueRequests;
        this.numOfCurrentConn = numOfCurrentConn;
        this.uniqueRequestList = uniqueRequestList;
        this.connectList = connectList;
        this.redirectList = redirectList;
    }

    public int getNumOfAllRequests() {
        return numOfAllRequests;
    }

    public void setNumOfAllRequests(int numOfAllRequests) {
        this.numOfAllRequests = numOfAllRequests;
    }

    public int getNumOfUniqueRequests() {
        return numOfUniqueRequests;
    }

    public void setNumOfUniqueRequests(int numOfUniqueRequests) {
        this.numOfUniqueRequests = numOfUniqueRequests;
    }

    public int getNumOfCurrentConn() {
        return numOfCurrentConn;
    }

    public void setNumOfCurrentConn(int numOfCurrentConn) {
        this.numOfCurrentConn = numOfCurrentConn;
    }

    public List<UniqueRequest> getUniqueRequestList() {
        return uniqueRequestList;
    }

    public void setUniqueRequestList(List<UniqueRequest> uniqueRequestList) {
        this.uniqueRequestList = uniqueRequestList;
    }

    public List<Connect> getConnectList() {
        return connectList;
    }

    public void setConnectList(List<Connect> connectList) {
        this.connectList = connectList;
    }

    public List<Redirect> getRedirectList() {
        return redirectList;
    }

    public void setRedirectList(List<Redirect> redirectList) {
        this.redirectList = redirectList;
    }
}
