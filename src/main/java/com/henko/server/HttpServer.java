package com.henko.server;

import com.henko.server.db.DBManager;
import com.henko.server.handler.ServerInitializer;
import com.henko.server.util.ConfigReader;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.IOException;

public class HttpServer {

    private final static ConfigReader CONFIG_READER = new ConfigReader("./config/server-config.properties");

    public void start() throws Exception {
        DBManager dbManager = new DBManager();
        dbManager.initialiseDB();

        // Configure the server
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap()
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializer());

            int port = _parsePort();

            Channel ch = b.bind(port).sync().channel();

            System.err.println("HTTP server started... \n" +
                    "navigate to http://127.0.0.1:" + port + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private int _parsePort() throws IOException {
        return Integer.parseInt(CONFIG_READER.getConfigs().getProperty("server.port"));
    }
}
