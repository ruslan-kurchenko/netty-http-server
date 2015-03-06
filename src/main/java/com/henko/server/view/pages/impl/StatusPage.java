package com.henko.server.view.pages.impl;

import com.henko.server.domain.ServerStatus;
import com.henko.server.domain.records.FirstTableRecord;
import com.henko.server.domain.records.SecondTableRecord;
import com.henko.server.domain.records.ThirdTableRecord;
import com.henko.server.view.pages.Page;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class StatusPage implements Page {

    private ServerStatus status;

    public StatusPage(ServerStatus status) {
        this.status = status;
    }

    @Override
    public ByteBuf getContent(Charset charset) {
        return Unpooled.copiedBuffer(generateContent(), charset);
    }

    private String generateContent() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Server Status</title>\n" +
                "</head>\n" +
                "<body>\n" +
                
                "    <div class=\"container\">\n" +
                "        <h2 style=\"text-align: center; \">SERVER STATUS</h2>\n" +
                "        <div class=\"top-info\">\n" +
                "            The total number of requests            - " + status.getAllRequests() + " <br>\n" +
                "            The number of unique queries            - " + status.getUniqueRequests() + "<br>\n" +
                "            The number of connections at the moment - " + status.getCurrentConnections() + "<br>\n" +
                "            <hr>\n" +
                "        </div>\n" +
                
                "        <!--first table: IP, number of connections, last connection-->\n" +
                "        <div class=\"table first\">\n" +
                "            <table border=\"1\">\n" +
                "                <caption>Table of count for certain IP connection</caption>\n" +
                "                <tr><th>IP</th><th>number of connection</th><th>last connection</th></tr>\n" +
                firstTableData() +
                "            </table>\n" +
                "            <hr>\n" +
                "        </div>\n" +
                
                "        <!--second table: URL, number of redirect-->\n" +
                "        <div class=\"table second\">\n" +
                "            <table border=\"1\">\n" +
                "                <caption>Table of count for certain redirection</caption>\n" +
                "                <tr><th>URL</th><th>number of redirection</th></tr>\n" +
                secondTableData() +
                "            </table>\n" +
                "            \n" +
                "            <hr>\n" +
                "        </div>\n" +
                
                "        <!--third table: IP, URI, timestamp, send bytes, received bytes, speed(byte/sec)-->\n" +
                "        <div class=\"table third\">\n" +
                "            <table border=\"1\">\n" +
                "                <caption>The table log of last 16 processed connections</caption>\n" +
                "                <tr>\n" +
                "                    <th>IP</th>\n" +
                "                    <th>URI</th>\n" +
                "                    <th>Time stamp</th>\n" +
                "                    <th>Send Bytes</th>\n" +
                "                    <th>Received Bytes</th>\n" +
                "                    <th>Speed (byte/sec)</th>\n" +
                "                </tr>\n" +
                thirdTableData() +
                "            </table>\n" +
                "            <hr>\n" +
                "        </div>\n" +
                
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private String firstTableData() {
        String data = "";
        for (FirstTableRecord record : status.getFirstTableData()) {
            data += "<tr><th>" + record.getIp() 
                    + "</th><th>" + record.getCount() 
                    + "</th><th>" + record.getLastConn() 
                    + "</th></tr>\n";
        }
        
        return data;
    }
    
    private String secondTableData() {
        String data = "";
        for (SecondTableRecord record : status.getSecondTableData()) {
            data += "<tr><th>" + record.getUrl()
                    + "</th><th>" + record.getNumberOfRedirect()
                    + "</th></tr>\n";
        }

        return data;
    }

    private String thirdTableData() {
        String data = "";
        for (ThirdTableRecord record : status.getThirdTableData()) {
            data += "<tr><th>" + record.getIp()
                    + "</th><th>" + record.getUri()
                    + "</th><th>" + record.getTimestamp()
                    + "</th><th>" + record.getSendBytes()
                    + "</th><th>" + record.getReceivedBytes()
                    + "</th><th>" + record.getSpeed()
                    + "</th></tr>\n";
        }

        return data;
    }

}
