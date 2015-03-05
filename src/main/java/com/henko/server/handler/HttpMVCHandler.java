package com.henko.server.handler;

import com.henko.server.controller.Controller;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpMVCHandler extends ChannelInboundHandlerAdapter {

    private static final Charset CONTENT_CHARSET = Charset.forName("UTF-8");
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ( !(msg instanceof HttpRequest) ) return;

        HttpRequest req = (HttpRequest) msg;

        if (is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }
        boolean keepAlive = isKeepAlive(req);
        
        URI uri = new URI(req.getUri());
        String path = uri.getPath();

        Controller controller = new Controller();
        ByteBuf pageContent = controller.getPageContent(path, CONTENT_CHARSET);
        
        FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, OK, pageContent);
        resp.headers().set(CONTENT_TYPE, "text/html");
        resp.headers().set(CONTENT_LENGTH, resp.content().readableBytes());

        if (!keepAlive) {
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
