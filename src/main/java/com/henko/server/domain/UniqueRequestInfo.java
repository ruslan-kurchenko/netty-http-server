package com.henko.server.domain;

import java.util.Date;

public class UniqueRequestInfo {
    private String ip;
    private int count;
    private Date lastConn;

    public UniqueRequestInfo() {
        
    }

    public UniqueRequestInfo(String ip, int count, Date lastConn) {
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
