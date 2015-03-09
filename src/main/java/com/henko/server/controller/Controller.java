package com.henko.server.controller;

import com.henko.server.dao.ConnectDao;
import com.henko.server.dao.RedirectDao;
import com.henko.server.dao.impl.DaoFactory;
import com.henko.server.domain.ServerStatus;
import com.henko.server.domain.UniqueRequest;
import com.henko.server.model.Connect;
import com.henko.server.model.Redirect;
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
    private DaoFactory _daoFactory;

    private final static int CONNECT_AMOUNT = 16;
    private final static int UNIQUE_REQ_AMOUNT = 3;
    private final static int REDIRECT_AMOUNT = 3;

    public Controller() {
        _daoFactory = getDaoFactory(H2);
    }
    
    public ByteBuf getPageContent(String path, Charset charset) throws InterruptedException {
        switch (path) {
            case PAGE_HELLO: return _processPageHello(charset);
            case PAGE_STATUS: return _processPageStatus(charset);
            
            default: return _processPageError(path, charset);
        }
    }

    public String parseRedirectUrl(HttpRequest req) throws URISyntaxException {
        URI uri = new URI(req.getUri());
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
        Map<String, List<String>> parameters = queryStringDecoder.parameters();

        if (!_validateParameters(parameters)) return " ";

        return "http://" + parameters.get("url").get(0);
    }

    private ByteBuf _processPageHello(Charset charset) throws InterruptedException {
        //Thread.sleep(10000);

        return new HelloPage().getContent(charset);
    }

    private ByteBuf _processPageStatus(Charset charset) {
        ServerStatus serverStatus = _prepareServerStatus();

        return new StatusPage(serverStatus).getContent(charset);
    }

    private ServerStatus _prepareServerStatus() {
        ConnectDao connDao = _daoFactory.getConnectionDao();
        List<Connect> connList = connDao.getLastNConn(CONNECT_AMOUNT);
        List<UniqueRequest> uniqueRequestList = connDao.getNUniqueRequest(UNIQUE_REQ_AMOUNT);
        int requests = connDao.getNumOfAllRequests();
        int uniqueRequest = connDao.getNumOfUniqueRequest();
        int currentConn = connDao.getNumOfCurrentConn();

        RedirectDao redirectDao = _daoFactory.getRedirectDao();
        List<Redirect> redirectList = redirectDao.getNRedirect(REDIRECT_AMOUNT);

        return new ServerStatus(requests, uniqueRequest, currentConn, uniqueRequestList, connList, redirectList);
    }

    private ByteBuf _processPageError(String path, Charset charset) {
        return new ErrorPage(path).getContent(charset);
    }

    private boolean _validateParameters(Map<String, List<String>> parameters) {
        return parameters.containsKey("url");
    }
}
