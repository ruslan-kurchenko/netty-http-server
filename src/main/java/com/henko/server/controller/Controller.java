package com.henko.server.controller;

import com.henko.server.dao.ConnectionInfoDao;
import com.henko.server.dao.RedirectInfoDao;
import com.henko.server.dao.impl.DaoFactory;
import com.henko.server.domain.ServerStatus;
import com.henko.server.domain.UniqueRequestInfo;
import com.henko.server.model.ConnectionInfo;
import com.henko.server.model.RedirectInfo;
import com.henko.server.view.impl.ErrorPage;
import com.henko.server.view.impl.HelloPage;
import com.henko.server.view.impl.StatusPage;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static com.henko.server.controller.PathRegistry.*;
import static com.henko.server.dao.impl.DaoFactory.*;

public class Controller {
    private DaoFactory daoFactory;

    public Controller() {
        daoFactory = getDaoFactory(H2);
    }
    
    public ByteBuf getPageContent(String path, Charset charset) throws InterruptedException {
        switch (path) {
            case PAGE_HELLO: return processPageHello(charset);
            case PAGE_STATUS: return processPageStatus(charset);
            
            default: return processPageError(path, charset);
        }
    }

    private ByteBuf processPageStatus(Charset charset) {
        ConnectionInfoDao connectionInfoDao = daoFactory.getConnectionInfoDao();
        List<ConnectionInfo> connectionInfoList = connectionInfoDao.selectLast16ConnInfo();
        List<UniqueRequestInfo> uniqueRequestInfoList = connectionInfoDao.selectUniqueRequestInfo();
        int numberOfAllRequests = connectionInfoDao.selectNumberOfAllRequests();
        int numberOfUniqueRequest = connectionInfoDao.selectNumberOfUniqueRequest();
        int numberOfCurrentConn = connectionInfoDao.selectNumberOfCurrentConn();

        RedirectInfoDao redirectInfoDao = daoFactory.getRedirectInfoDao();
        List<RedirectInfo> redirectInfoList = redirectInfoDao.selectAll();

        ServerStatus serverStatus = new ServerStatus();
        serverStatus.setNumberOfAllRequests(numberOfAllRequests);
        serverStatus.setNumberOfUniqueRequests(numberOfUniqueRequest);
        serverStatus.setNumberOfCurrentConnections(numberOfCurrentConn);
        serverStatus.setConnectionInfoList(connectionInfoList);
        serverStatus.setRedirectInfoList(redirectInfoList);
        serverStatus.setUniqueRequestInfoList(uniqueRequestInfoList);



        return new StatusPage(serverStatus).getContent(charset);
    }

    private ByteBuf processPageHello(Charset charset) throws InterruptedException {
        //Thread.sleep(10000);

        return new HelloPage().getContent(charset);
    }
    
    private ByteBuf processPageError(String path, Charset charset) {
        return new ErrorPage(path).getContent(charset);
    }

    public String parseRedirectUrl(HttpRequest req) throws URISyntaxException {
        URI uri = new URI(req.getUri());
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
        Map<String, List<String>> parameters = queryStringDecoder.parameters();

        if (!validateParameters(parameters)) return " ";

        return "http://" + parameters.get("url").get(0);
    }

    private boolean validateParameters(Map<String, List<String>> parameters) {
        return parameters.containsKey("url");
    }
}
