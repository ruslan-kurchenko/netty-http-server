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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UniqueRequestInfo that = (UniqueRequestInfo) o;

        if (count != that.count) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (lastConn != null ? !lastConn.equals(that.lastConn) : that.lastConn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + count;
        result = 31 * result + (lastConn != null ? lastConn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UniqueRequestInfo{" +
                "ip='" + ip + '\'' +
                ", count=" + count +
                ", lastConn=" + lastConn +
                '}';
    }
}
