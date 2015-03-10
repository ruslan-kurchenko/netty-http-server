package com.henko.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;

import java.util.concurrent.atomic.AtomicInteger;

import static io.netty.channel.ChannelHandler.*;

@Sharable
public class ServerConnectionCountHandler extends ChannelTrafficShapingHandler {

    private static final AtomicInteger CURRENT_CONNECTION_COUNT = new AtomicInteger();
    private static final AtomicInteger ALL_CONNECTION_COUNT = new AtomicInteger();

    public ServerConnectionCountHandler(long checkInterval) {
        super(checkInterval);
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        CURRENT_CONNECTION_COUNT.incrementAndGet();
        ALL_CONNECTION_COUNT.incrementAndGet();

        super.handlerAdded(ctx);
    }

    @Override
    public synchronized void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);

        CURRENT_CONNECTION_COUNT.decrementAndGet();
    }

    public static int getConnectionCount(){
        return CURRENT_CONNECTION_COUNT.get();
    }

    public static int getAllConnCount() {
        return ALL_CONNECTION_COUNT.get();
    }
}
