package com.henko.server.handler;

import com.henko.server.controller.Controller;
import com.henko.server.dao.exception.PersistException;
import com.henko.server.model.Connect;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

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

    private final Connect _connInfo;
    private final Controller _controller = new Controller();

    public HttpMVCHandler(Connect connInfo) {
        this._connInfo = connInfo;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ( !(msg instanceof HttpRequest) ) return;
        HttpRequest req = (HttpRequest) msg;

        _saveConnInfoData(ctx, req);

        if (is100ContinueExpected(req)) _write100ContinueResponse(ctx);

        String clientPath = _parseClientPath(req);

        if (_isRedirect(clientPath)) {
            _processRedirect(ctx, req);
            return;
        }

        ByteBuf pageContent = _controller.getPageContent(clientPath);
        FullHttpResponse resp = _generateFullHttpResponse(pageContent);

        _writeFullHttpResponse(ctx, req, resp);
    }

    private void _saveConnInfoData(ChannelHandlerContext ctx, HttpRequest req) throws URISyntaxException {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = inetSocketAddress.getAddress().getHostAddress();
        String uri = _parseClientPath(req);

        _connInfo.setIp(ip);
        _connInfo.setUri(uri);
    }

    private void _write100ContinueResponse(ChannelHandlerContext ctx) {
        ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
    }

    private String _parseClientPath(HttpRequest req) throws URISyntaxException {
        URI uri = new URI(req.getUri().toLowerCase());
        return uri.getPath();
    }

    private boolean _isRedirect(String clientPath) {
        return REDIRECT.equals(clientPath);
    }

    private void _processRedirect(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
        String url = _controller.parseRedirectUrl(req);

        _saveRedirectInfoData(url);

        FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        resp.headers().set(LOCATION, url);
        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }

    private void _saveRedirectInfoData(String url) throws PersistException {
        getDaoFactory(H2).getRedirectDao().addOrIncrementCount(url);
    }

    private FullHttpResponse _generateFullHttpResponse(ByteBuf pageContent) {
        FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, OK, pageContent);
        resp.headers().set(CONTENT_TYPE, "text/html");
        resp.headers().set(CONTENT_LENGTH, resp.content().readableBytes());

        return resp;
    }

    private void _writeFullHttpResponse(ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse resp) {
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
