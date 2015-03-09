package com.henko.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class NumberConnectionHandler extends ChannelTrafficShapingHandler {

    private static final AtomicInteger _CONNECTION_COUNT = new AtomicInteger();

    public NumberConnectionHandler(long checkInterval) {
        super(checkInterval);
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        _CONNECTION_COUNT.incrementAndGet();

        super.handlerAdded(ctx);
    }

    @Override
    public synchronized void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);

        _CONNECTION_COUNT.decrementAndGet();
    }

    public static int getConnectionCount(){
        return _CONNECTION_COUNT.get();
    }
}
