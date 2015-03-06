package com.henko.server.domain.records;

public class SecondTableRecord {
    private String url;
    private int numberOfRedirect;

    public SecondTableRecord() {
    }

    public SecondTableRecord(String url, int numberOfRedirect) {
        this.url = url;
        this.numberOfRedirect = numberOfRedirect;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumberOfRedirect() {
        return numberOfRedirect;
    }

    public void setNumberOfRedirect(int numberOfRedirect) {
        this.numberOfRedirect = numberOfRedirect;
    }
}
