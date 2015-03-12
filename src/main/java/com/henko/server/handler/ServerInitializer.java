package com.henko.server.handler;

import com.henko.server.model.Connect;
import com.henko.server.model.UniqueReq;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;


public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final Connect connect = new Connect();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new ServerTrafficHandler(connect))
                .addLast(new ServerDataBaseCleaner(8000, 10000))
                .addLast(new ServerConnectionCountHandler())
                .addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(512 * 1024))
                .addLast(new ServerHttpRequestHandler(connect));
    }
}
