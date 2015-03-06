package com.henko.server.domain.records;

import java.util.Date;

public class FirstTableRecord {
    private String ip;
    private int count;
    private Date lastConn;

    public FirstTableRecord() {
        
    }

    public FirstTableRecord(String ip, int count, Date lastConn) {
        this.ip = ip;
        this.count = count;
        this.lastConn = lastConn;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getLastConn() {
        return lastConn;
    }

    public void setLastConn(Date lastConn) {
        this.lastConn = lastConn;
    }
}
