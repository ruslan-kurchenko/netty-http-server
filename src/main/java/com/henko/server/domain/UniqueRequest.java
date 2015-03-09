package com.henko.server.domain;

import java.util.Date;

public class UniqueRequest {
    private String ip;
    private int count;
    private long lastConn;

    public UniqueRequest() {
        
    }

    public UniqueRequest(String ip, int count, long lastConn) {
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

    public long getLastConn() {
        return lastConn;
    }

    public void setLastConn(long lastConn) {
        this.lastConn = lastConn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UniqueRequest that = (UniqueRequest) o;

        if (count != that.count) return false;
        if (lastConn != that.lastConn) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + count;
        result = 31 * result + (int) (lastConn ^ (lastConn >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "UniqueRequest{" +
                "ip='" + ip + '\'' +
                ", count=" + count +
                ", lastConn=" + new Date(lastConn) +
                '}';
    }
}
