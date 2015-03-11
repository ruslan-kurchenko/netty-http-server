package com.henko.server.model;


public class UniqueReq {
    private int id;
    private String ip;
    private int count;
    private long lastConn;

    public UniqueReq() {
        
    }

    public UniqueReq(String ip, long lastConn) {
        this.ip = ip;
        this.lastConn = lastConn;
    }

    public UniqueReq(String ip, int count, long lastConn) {
        this.ip = ip;
        this.count = count;
        this.lastConn = lastConn;
    }

    public UniqueReq(int id, String ip, int count, long lastConn) {
        this.id = id;
        this.ip = ip;
        this.count = count;
        this.lastConn = lastConn;
    }

    public int getId() {
        return id;
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

        UniqueReq uniqueReq = (UniqueReq) o;

        if (count != uniqueReq.count) return false;
        if (id != uniqueReq.id) return false;
        if (lastConn != uniqueReq.lastConn) return false;
        if (ip != null ? !ip.equals(uniqueReq.ip) : uniqueReq.ip != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + count;
        result = 31 * result + (int) (lastConn ^ (lastConn >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "UniqueReq{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", count=" + count +
                ", lastConn=" + lastConn +
                '}';
    }
}
