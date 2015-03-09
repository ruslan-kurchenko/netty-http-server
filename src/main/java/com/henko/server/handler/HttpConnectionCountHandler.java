package com.henko.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class HttpConnectionCountHandler extends ChannelTrafficShapingHandler {

    private static final AtomicInteger connectionCount = new AtomicInteger();

    public HttpConnectionCountHandler(long checkInterval) {
        super(checkInterval);
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        connectionCount.incrementAndGet();

        super.handlerAdded(ctx);
    }

    @Override
    public synchronized void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);

        connectionCount.decrementAndGet();
    }

    public static int getConnectionCount(){
        return connectionCount.get();
    }
}
