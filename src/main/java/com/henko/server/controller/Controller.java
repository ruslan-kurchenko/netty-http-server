package com.henko.server.controller;

import com.henko.server.dao.ConnectDao;
import com.henko.server.dao.RedirectDao;
import com.henko.server.dao.impl.DaoFactory;
import com.henko.server.domain.ServerStatus;
import com.henko.server.domain.UniqueRequest;
import com.henko.server.model.Connect;
import com.henko.server.model.Redirect;
import com.henko.server.view.ErrorPage;
import com.henko.server.view.HelloPage;
import com.henko.server.view.StatusPage;
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
    private DaoFactory _daoFactory;

    private final static int CONNECT_AMOUNT = 16;
    private final static int UNIQUE_REQ_AMOUNT = 3;
    private final static int REDIRECT_AMOUNT = 3;

    private static final Charset CONTENT_CHARSET = Charset.forName("UTF-8");

    public Controller() {
        _daoFactory = getDaoFactory(H2);
    }
    
    public ByteBuf getPageContent(String path) throws InterruptedException {
        switch (path) {
            case PAGE_HELLO: return _processPageHello();
            case PAGE_STATUS: return _processPageStatus();
            
            default: return _processPageError(path);
        }
    }

    public String parseRedirectUrl(HttpRequest req) throws URISyntaxException {
        URI uri = new URI(req.getUri());
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
        Map<String, List<String>> parameters = queryStringDecoder.parameters();

        if (!_validateParameters(parameters)) return " ";

        return "http://" + parameters.get("url").get(0);
    }

    private ByteBuf _processPageHello() throws InterruptedException {
        //Thread.sleep(10000);

        return new HelloPage().getContent(CONTENT_CHARSET);
    }

    private ByteBuf _processPageStatus() {
        ServerStatus serverStatus = _prepareServerStatus();

        return new StatusPage(serverStatus).getContent(CONTENT_CHARSET);
    }

    private ByteBuf _processPageError(String path) {
        return new ErrorPage(path).getContent(CONTENT_CHARSET);
    }

    private ServerStatus _prepareServerStatus() {
        ConnectDao connDao = _daoFactory.getConnectionDao();
        RedirectDao redirectDao = _daoFactory.getRedirectDao();

        List<Connect> connList = connDao.getLastNConn(CONNECT_AMOUNT);
        List<UniqueRequest> uniqueRequestList = connDao.getNUniqueRequest(UNIQUE_REQ_AMOUNT);
        int requests = connDao.getNumOfAllConn();
        int uniqueRequest = connDao.getNumOfUniqueConn();
        int currentConn = connDao.getNumOfCurrentConn();
        List<Redirect> redirectList = redirectDao.getNRedirect(REDIRECT_AMOUNT);

        return new ServerStatus(requests, uniqueRequest, currentConn, uniqueRequestList, connList, redirectList);
    }

    private boolean _validateParameters(Map<String, List<String>> parameters) {
        return parameters.containsKey("url");
    }
}
