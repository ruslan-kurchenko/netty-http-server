package com.henko.server.model;

import java.io.Serializable;

public class Connect implements Serializable {
    private int id;
    private String ip;
    private String uri;
    private long timestamp;
    private long sendBytes;
    private long receivedBytes;
    private long speed;

    public Connect() {
        
    }

    public Connect(int id, String ip, String uri,
                   long timestamp, long sendBytes,
                   long receivedBytes, long speed) {
        this.id = id;
        this.ip = ip;
        this.uri = uri;
        this.timestamp = timestamp;
        this.sendBytes = sendBytes;
        this.receivedBytes = receivedBytes;
        this.speed = speed;
    }

    public Connect(String ip, String uri,
                   long timestamp, long sendBytes,
                   long receivedBytes, long speed) {
        this.ip = ip;
        this.uri = uri;
        this.timestamp = timestamp;
        this.sendBytes = sendBytes;
        this.receivedBytes = receivedBytes;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Connect that = (Connect) o;

        if (id != that.id) return false;
        if (receivedBytes != that.receivedBytes) return false;
        if (sendBytes != that.sendBytes) return false;
        if (speed != that.speed) return false;
        if (timestamp != that.timestamp) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (uri != null ? !uri.equals(that.uri) : that.uri != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (int) (sendBytes ^ (sendBytes >>> 32));
        result = 31 * result + (int) (receivedBytes ^ (receivedBytes >>> 32));
        result = 31 * result + (int) (speed ^ (speed >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ConnectionInfo{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", uri='" + uri + '\'' +
                ", timestamp=" + timestamp +
                ", sendBytes=" + sendBytes +
                ", receivedBytes=" + receivedBytes +
                ", speed=" + speed +
                '}';
    }
}
