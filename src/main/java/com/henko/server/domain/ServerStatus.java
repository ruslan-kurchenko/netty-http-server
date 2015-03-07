package com.henko.server.domain;

import java.util.List;

public class ServerStatus {
    private int numberOfAllRequests;
    private int numberOfUniqueRequests;
    private int numberOfCurrentConnections;
    private List<UniqueRequestInfo> firstTableData;

    public ServerStatus() {
    }

}
