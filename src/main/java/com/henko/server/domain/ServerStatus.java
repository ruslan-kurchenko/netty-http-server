package com.henko.server.domain;

import com.henko.server.model.ConnectionInfo;
import com.henko.server.model.RedirectInfo;

import java.util.List;

public class ServerStatus {
    private int numberOfAllRequests;
    private int numberOfUniqueRequests;
    private int numberOfCurrentConnections;
    private List<UniqueRequestInfo> uniqueRequestInfoList;
    private List<ConnectionInfo> connectionInfoList;
    private List<RedirectInfo> redirectInfoList;

    public ServerStatus() {
    }

    public ServerStatus(int numberOfAllRequests,
                        int numberOfUniqueRequests,
                        int numberOfCurrentConnections,
                        List<UniqueRequestInfo> uniqueRequestInfoList,
                        List<ConnectionInfo> connectionInfoList,
                        List<RedirectInfo> redirectInfoList) {
        this.numberOfAllRequests = numberOfAllRequests;
        this.numberOfUniqueRequests = numberOfUniqueRequests;
        this.numberOfCurrentConnections = numberOfCurrentConnections;
        this.uniqueRequestInfoList = uniqueRequestInfoList;
        this.connectionInfoList = connectionInfoList;
        this.redirectInfoList = redirectInfoList;
    }

    public int getNumberOfAllRequests() {
        return numberOfAllRequests;
    }

    public void setNumberOfAllRequests(int numberOfAllRequests) {
        this.numberOfAllRequests = numberOfAllRequests;
    }

    public int getNumberOfUniqueRequests() {
        return numberOfUniqueRequests;
    }

    public void setNumberOfUniqueRequests(int numberOfUniqueRequests) {
        this.numberOfUniqueRequests = numberOfUniqueRequests;
    }

    public int getNumberOfCurrentConnections() {
        return numberOfCurrentConnections;
    }

    public void setNumberOfCurrentConnections(int numberOfCurrentConnections) {
        this.numberOfCurrentConnections = numberOfCurrentConnections;
    }

    public List<UniqueRequestInfo> getUniqueRequestInfoList() {
        return uniqueRequestInfoList;
    }

    public void setUniqueRequestInfoList(List<UniqueRequestInfo> uniqueRequestInfoList) {
        this.uniqueRequestInfoList = uniqueRequestInfoList;
    }

    public List<ConnectionInfo> getConnectionInfoList() {
        return connectionInfoList;
    }

    public void setConnectionInfoList(List<ConnectionInfo> connectionInfoList) {
        this.connectionInfoList = connectionInfoList;
    }

    public List<RedirectInfo> getRedirectInfoList() {
        return redirectInfoList;
    }

    public void setRedirectInfoList(List<RedirectInfo> redirectInfoList) {
        this.redirectInfoList = redirectInfoList;
    }
}
