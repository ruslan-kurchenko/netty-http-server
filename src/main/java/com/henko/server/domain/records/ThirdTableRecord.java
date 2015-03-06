package com.henko.server.domain.records;

import java.util.Date;

public class ThirdTableRecord {
    private String ip;
    private String uri;
    private Date timestamp;
    private long sendBytes;
    private long receivedBytes;
    private long speed;

    public ThirdTableRecord() {
    }

    public ThirdTableRecord(String ip, String uri,
                            Date timestamp, long sendBytes, 
                            long receivedBytes, long speed) {
        this.ip = ip;
        this.uri = uri;
        this.timestamp = timestamp;
        this.sendBytes = sendBytes;
        this.receivedBytes = receivedBytes;
        this.speed = speed;
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

    public long getSendBytes() {
        return sendBytes;
    }

    public void setSendBytes(long sendBytes) {
        this.sendBytes = sendBytes;
    }

    public long getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(long receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }
}
