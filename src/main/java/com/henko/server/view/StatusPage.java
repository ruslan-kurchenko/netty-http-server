package com.henko.server.view;

import com.henko.server.domain.ServerStatus;
import com.henko.server.model.UniqueReq;
import com.henko.server.model.Connect;
import com.henko.server.model.Redirect;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StatusPage implements Page {

    private final ServerStatus _status;
    private final SimpleDateFormat _dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SS");

    public StatusPage(ServerStatus status) {
        this._status = status;
    }

    @Override
    public ByteBuf getContent(Charset charset) {
        return Unpooled.copiedBuffer(_generateContent(), charset);
    }

    private String _generateContent() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +

                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Server Status</title>\n" +
                _getPageStyle() +
                "</head>\n" +

                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h2 style=\"\">SERVER STATUS</h2>\n" +

                "        <div class=\"top-info\">\n" +
                _getTopInfo() +
                "        </div>\n" +

                "        <hr>\n" +

                "        <!--first table: IP, connections, last connection-->\n" +
                "        <div class=\"section first\">\n" +
                _getUniqueConnTable() +
                "        </div>\n" +

                "        <!--second table: URL, number of redirect-->\n" +
                "        <div class=\"section second\">\n" +
                _getRedirectTable() +
                "        </div>\n" +

                "        <!--third table: IP, URI, timestamp, send bytes, received bytes, speed(byte/sec)      -->\n" +
                "        <div class=\"section third\">\n" +
                _getLastConnTable() +
                "        </div>\n" +

                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private String _getTopInfo() {
        return  "<ul>\n" +
                "   <li>Total requests: " + _status.getNumOfAllRequests() + "</li>\n" +
                "   <li>Unique requests: " + _status.getNumOfUniqueRequests() + "</li>\n" +
                "   <li>Current connections: " + _status.getNumOfCurrentConn() + "</li>\n" +
                "</ul>\n";
    }

    private String _getUniqueConnTable() {

        return  "<table>\n" +
                "  <caption>Table of connections from a certain IP</caption>\n" +
                "     <tr>\n" +
                "        <th>IP</th>\n" +
                "        <th>Connections</th>\n" +
                "        <th>Last connection</th>\n" +
                "     </tr>\n" +
                _getContentOfUniqueRequestsTable() +
                "</table>\n";
    }

    private String _getRedirectTable() {
        return  "<table>\n" +
                "  <caption>Table of redirects</caption>\n" +
                "     <tr>\n" +
                "        <th>URL</th>\n" +
                "        <th>number of redirection</th>\n" +
                "     </tr>\n" +
                _getContentOfRedirectsTable() +
                "</table>\n";
    }

    private String _getLastConnTable() {
        return  "<table>\n" +
                "  <caption>Table of last connection</caption>\n" +
                "     <tr>\n" +
                "        <th>IP</th>\n" +
                "        <th>URI</th>\n" +
                "        <th>Time stamp</th>\n" +
                "        <th>Send Bytes</th>\n" +
                "        <th>Received Bytes</th>\n" +
                "        <th>Speed (byte/sec)</th>\n" +
                "     </tr>\n" +
                _getContentOfConnectionsTable() +
                "</table>\n";
    }

    private String _getContentOfUniqueRequestsTable() {
        String data = "";
        List<UniqueReq> list = _status.getUniqueReqList();

        if (list == null) return _getEmptyTableColumns(3);

        for (UniqueReq request : list) {
            data += "<tr>\n" +
                    "   <td>" + request.getIp() + "</td>\n" +
                    "   <td>" + request.getCount() + "</td>\n" +
                    "   <td>" + _formatTime(request.getLastConn()) + "</td>\n" +
                    "</tr>\n";
        }

        return data;
    }

    private String _getContentOfRedirectsTable() {
        String data = "";
        List<Redirect> list = _status.getRedirectList();

        if (list == null) return _getEmptyTableColumns(2);

        for (Redirect redirect : list) {
            data += "<tr>\n" +
                    "   <td>" + redirect.getUrl() + "</td>\n" +
                    "   <td>" + redirect.getCount() + "</td>\n" +
                    "</tr>\n";
        }

        return data;
    }

    private String _getContentOfConnectionsTable() {
        String data = "";
        List<Connect> list = _status.getConnectList();

        if (list == null) return _getEmptyTableColumns(6);

        for (Connect connect : list) {
            data += "<tr>\n" +
                    "   <td>" + connect.getIp() + "</td>\n" +
                    "   <td>" + connect.getUri() + "</td>\n" +
                    "   <td>" + _formatTime(connect.getTimestamp()) + "</td>\n" +
                    "   <td>" + connect.getSendBytes() + "</td>\n" +
                    "   <td>" + connect.getReceivedBytes() + "</td>\n" +
                    "   <td>" + connect.getSpeed() + "</td>\n" +
                    "</tr>\n";
        }

        return data;
    }

    private String _getPageStyle() {
        return  "" +
                "   <style>\n" +
                "        .top-info { width: 380px; text-align: left; margin: auto; }\n" +
                "        body { width: 800px; margin: auto; text-align: center;\n" +
                "            font-family: 'Helvetica Neue', Helvetica, Arial;\n" +
                "            font-size: 16px; line-height: 25px;color: #555555; background: #f8f8f8; }\n" +
                "        caption { margin-bottom: 5px; }\n" +
                "        .section { margin: 20px 0; }\n" +
                "        table { width: 100%; }\n" +
                "        th { background-color: #20B2AA; }\n" +
                "        tr { background-color: #eeeeee; }\n" +
                "        tr:nth-child(2n) { background-color: #dddddd; }\n" +
                "        th, td { padding: 3px; }\n" +
                "    </style>\n";
    }

    private String _getEmptyTableColumns(int count){
        StringBuilder str = new StringBuilder();

        str.append("<tr>");
        for (int i = 0; i < count; i++) {
            str.append("<th></th>");
        }
        str.append("</tr>");

        return str.toString();
    }


    private String _formatTime(long time) {
        return _dateFormat.format(new Date(time));
    }
}
