package com.henko.server.model;

import java.io.Serializable;
import java.util.Date;

public class ConnectionInfo implements Serializable {
    private int id;
    private String ip;
    private String uri;
    private Date timestamp;
    private long send_bytes;
    private long received_bytes;
    private long speed;

    public ConnectionInfo() {
        
    }

    public ConnectionInfo(int id, String ip, String uri, 
                          Date timestamp, long send_bytes, 
                          long received_bytes, long speed) {
        this.id = id;
        this.ip = ip;
        this.uri = uri;
        this.timestamp = timestamp;
        this.send_bytes = send_bytes;
        this.received_bytes = received_bytes;
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getSend_bytes() {
        return send_bytes;
    }

    public void setSend_bytes(long send_bytes) {
        this.send_bytes = send_bytes;
    }

    public long getReceived_bytes() {
        return received_bytes;
    }

    public void setReceived_bytes(long received_bytes) {
        this.received_bytes = received_bytes;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }
}
