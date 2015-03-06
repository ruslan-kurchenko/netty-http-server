package com.henko.server.domain;

import com.henko.server.domain.records.FirstTableRecord;
import com.henko.server.domain.records.SecondTableRecord;
import com.henko.server.domain.records.ThirdTableRecord;

import java.util.List;

public class ServerStatus {
    private int allRequests;
    private int uniqueRequests;
    private int currentConnections;
    private List<FirstTableRecord> firstTableData;
    private List<SecondTableRecord> secondTableData;
    private List<ThirdTableRecord> thirdTableData;

    public ServerStatus() {
    }

    public ServerStatus(int allRequests,
                        int uniqueRequests, 
                        int currentConnections, 
                        List<FirstTableRecord> firstTableData, 
                        List<SecondTableRecord> secondTableData, 
                        List<ThirdTableRecord> thirdTableData) {
        this.allRequests = allRequests;
        this.uniqueRequests = uniqueRequests;
        this.currentConnections = currentConnections;
        this.firstTableData = firstTableData;
        this.secondTableData = secondTableData;
        this.thirdTableData = thirdTableData;
    }

    public int getAllRequests() {
        return allRequests;
    }

    public void setAllRequests(int allRequests) {
        this.allRequests = allRequests;
    }

    public int getUniqueRequests() {
        return uniqueRequests;
    }

    public void setUniqueRequests(int uniqueRequests) {
        this.uniqueRequests = uniqueRequests;
    }

    public int getCurrentConnections() {
        return currentConnections;
    }

    public void setCurrentConnections(int currentConnections) {
        this.currentConnections = currentConnections;
    }

    public List<FirstTableRecord> getFirstTableData() {
        return firstTableData;
    }

    public void setFirstTableData(List<FirstTableRecord> firstTableData) {
        this.firstTableData = firstTableData;
    }

    public List<SecondTableRecord> getSecondTableData() {
        return secondTableData;
    }

    public void setSecondTableData(List<SecondTableRecord> secondTableData) {
        this.secondTableData = secondTableData;
    }

    public List<ThirdTableRecord> getThirdTableData() {
        return thirdTableData;
    }

    public void setThirdTableData(List<ThirdTableRecord> thirdTableData) {
        this.thirdTableData = thirdTableData;
    }
}
