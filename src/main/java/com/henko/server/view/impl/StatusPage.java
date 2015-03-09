package com.henko.server.view.impl;

import com.henko.server.domain.ServerStatus;
import com.henko.server.domain.UniqueRequestInfo;
import com.henko.server.model.ConnectionInfo;
import com.henko.server.model.RedirectInfo;
import com.henko.server.view.Page;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

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
                "            The total number of requests            - " + status.getNumberOfAllRequests() + " <br>\n" +
                "            The number of unique queries            - " + status.getNumberOfUniqueRequests() + "<br>\n" +
                "            The number of connections at the moment - " + status.getNumberOfCurrentConnections() + "<br>\n" +
                "            <hr>\n" +
                "        </div>\n" +

                "        <!--first table: IP, number of connections, last connection-->\n" +
                "        <div class=\"table first\">\n" +
                "            <table border=\"1\">\n" +
                "                <caption>Table of count for certain IP connection</caption>\n" +
                "                <tr><th>IP</th><th>number of connection</th><th>last connection</th></tr>\n" +
                generateTableOfUniqueRequests() +
                "            </table>\n" +
                "            <hr>\n" +
                "        </div>\n" +

                "        <!--second table: URL, number of redirect-->\n" +
                "        <div class=\"table second\">\n" +
                "            <table border=\"1\">\n" +
                "                <caption>Table of count for certain redirection</caption>\n" +
                "                <tr><th>URL</th><th>number of redirection</th></tr>\n" +
                generateTableOfRedirects() +
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
                generateTableOfConnection() +
                "            </table>\n" +
                "            <hr>\n" +
                "        </div>\n" +

                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private String generateTableOfUniqueRequests() {
        String data = "";
        List<UniqueRequestInfo> list = status.getUniqueRequestInfoList();

        if (list == null) {
            data += "<tr><th>\t\r</th><th>\t\r</th><th>\t\r</th></tr>";

        } else {
            for (UniqueRequestInfo info : list) {
                data += "<tr><th>" + info.getIp()
                        + "</th><th>" + info.getCount()
                        + "</th><th>" + info.getLastConn()
                        + "</th></tr>\n";
            }
        }

        return data;
    }

    private String generateTableOfRedirects() {
        String data = "";
        List<RedirectInfo> list = status.getRedirectInfoList();

        if (list == null) {
            data += "<tr><th></th><th></th></tr>";
        } else {
            for (RedirectInfo info : list) {
                data += "<tr><th>" + info.getUrl()
                        + "</th><th>" + info.getCount()
                        + "</th></tr>\n";
            }
        }

        return data;
    }

    private String generateTableOfConnection() {
        String data = "";
        List<ConnectionInfo> list = status.getConnectionInfoList();

        if (list == null) {
            data += "<tr><th></th><th></th><th></th><th></th><th></th><th></th></tr>";
        } else {
            for (ConnectionInfo info : list) {
                data += "<tr><th>" + info.getIp()
                        + "</th><th>" + info.getUri()
                        + "</th><th>" + new Date(info.getTimestamp())
                        + "</th><th>" + info.getSendBytes()
                        + "</th><th>" + info.getReceivedBytes()
                        + "</th><th>" + info.getSpeed()
                        + "</th></tr>\n";
            }
        }
        return data;
    }
}
