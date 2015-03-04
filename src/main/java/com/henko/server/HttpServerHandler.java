package com.henko.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

import java.nio.charset.Charset;

import static io.netty.handler.codec.http.HttpHeaders.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    private static final String PAGE_HELLO = "HELLO WORLD";
    
    private static final String CONTENT_CHARSET = "UTF-8";
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ( !(msg instanceof HttpRequest) ) return;
        
        HttpRequest req = (HttpRequest) msg;
        
        if (is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }
        boolean keepAlive = isKeepAlive(req);
        FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.copiedBuffer(PAGE_HELLO, getContentCharset() ));
        resp.headers().set(CONTENT_TYPE, "text/html");
        resp.headers().set(CONTENT_LENGTH, getContentLength(resp));
        
        if (!keepAlive) {
            ctx.write(resp).addListener(ChannelFutureListener.CLOSE);
        } else {
            resp.headers().set(CONNECTION, Values.KEEP_ALIVE);
            ctx.write(resp);
        }
    }

    private int getContentLength(FullHttpResponse resp) {
        return resp.content().readableBytes();
    }

    private Charset getContentCharset () {
        return Charset.forName(CONTENT_CHARSET);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    
    
}
