package com.henko.server.handler;

import com.henko.server.controller.Controller;
import com.henko.server.controller.PathRegistry;
import com.henko.server.dao.ConnectionInfoDao;
import com.henko.server.dao.impl.DaoFactory;
import com.henko.server.model.ConnectionInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static com.henko.server.controller.PathRegistry.*;
import static com.henko.server.dao.impl.DaoFactory.*;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.removeHeader;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpMVCHandler extends ChannelInboundHandlerAdapter {

    private final ConnectionInfo connInfo;
    private final Controller controller;

    private static final Charset CONTENT_CHARSET = Charset.forName("UTF-8");

    public HttpMVCHandler(ConnectionInfo connInfo) {
        this.connInfo = connInfo;
        this.controller = new Controller();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ( !(msg instanceof HttpRequest) ) return;
        HttpRequest req = (HttpRequest) msg;

        saveConnInfoData(ctx, req);

        if (is100ContinueExpected(req)) write100ContinueResponse(ctx);

        String clientPath = parseClientPath(req);

        if (isRedirect(clientPath)) {
            processRedirect(ctx, req);
            return;
        }

        ByteBuf pageContent = controller.getPageContent(clientPath, CONTENT_CHARSET);
        FullHttpResponse resp = generateFullHttpResponse(pageContent);

        writeFullHttpResponse(ctx, req, resp);
    }

    private void saveConnInfoData(ChannelHandlerContext ctx, HttpRequest req) throws URISyntaxException {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = inetSocketAddress.getAddress().getHostAddress();
        String uri = parseClientPath(req);

        connInfo.setIp(ip);
        connInfo.setUri(uri);
    }

    private void write100ContinueResponse(ChannelHandlerContext ctx) {
        ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
    }

    private String parseClientPath(HttpRequest req) throws URISyntaxException {
        URI uri = new URI(req.getUri().toLowerCase());
        return uri.getPath();
    }

    private boolean isRedirect(String clientPath) {
        return REDIRECT.equals(clientPath);
    }

    private void processRedirect(ChannelHandlerContext ctx, HttpRequest req) throws URISyntaxException {
        String url = controller.parseRedirectUrl(req);
        FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        resp.headers().set(LOCATION, url);
        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }

    private FullHttpResponse generateFullHttpResponse(ByteBuf pageContent) {
        FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, OK, pageContent);
        resp.headers().set(CONTENT_TYPE, "text/html");
        resp.headers().set(CONTENT_LENGTH, resp.content().readableBytes());

        return resp;
    }

    private void writeFullHttpResponse(ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse resp) {
        if (!isKeepAlive(req)) {
            ctx.write(resp).addListener(ChannelFutureListener.CLOSE);
        } else {
            resp.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            ctx.write(resp);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
