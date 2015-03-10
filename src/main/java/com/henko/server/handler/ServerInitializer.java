package com.henko.server.handler;

import com.henko.server.model.Connect;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;


public class ServerInitializer extends ChannelInitializer<SocketChannel>{

    private final Connect connect = new Connect();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new ServerTrafficHandler(0, connect));
        p.addLast(new ServerDataBaseCleaner());
        p.addLast(new ServerConnectionCountHandler(0));
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(512 * 1024));
        p.addLast(new ServerHttpRequestHandler(connect));
    }
}
