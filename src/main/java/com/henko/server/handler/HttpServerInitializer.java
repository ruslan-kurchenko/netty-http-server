package com.henko.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;


public class HttpServerInitializer extends ChannelInitializer<SocketChannel>{
    
    private final SslContext sslCtx;

    public HttpServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) p.addLast(sslCtx.newHandler(ch.alloc()));
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpMVCHandler());
    }
}
