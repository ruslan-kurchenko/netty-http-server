package com.henko.server.handler;

import com.henko.server.model.Connect;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;


public class HttpServerInitializer extends ChannelInitializer<SocketChannel>{
    
    private final SslContext _sslCtx;
    private final Connect _i_connection = new Connect();

    public HttpServerInitializer(SslContext _sslCtx) {
        this._sslCtx = _sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if (_sslCtx != null) p.addLast(_sslCtx.newHandler(ch.alloc()));

        p.addLast(new ServerTrafficHandler(0, _i_connection));
        p.addLast(new HttpDataBaseCleaner());
        p.addLast(new NumberConnectionHandler(0));
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpMVCHandler(_i_connection));
    }
}
