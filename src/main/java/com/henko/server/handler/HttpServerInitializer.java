package com.henko.server.handler;

import com.henko.server.model.ConnectionInfo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;


public class HttpServerInitializer extends ChannelInitializer<SocketChannel>{
    
    private final SslContext sslCtx;
    private final ConnectionInfo connectionInfo;

    public HttpServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
        this.connectionInfo = new ConnectionInfo();
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) p.addLast(sslCtx.newHandler(ch.alloc()));

        p.addLast(new HttpTrafficCounter(0, connectionInfo));
        p.addLast(new HttpConnectionCountHandler(0));
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpMVCHandler(connectionInfo));
    }
}
