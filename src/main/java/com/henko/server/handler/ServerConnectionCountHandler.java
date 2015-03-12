package com.henko.server.handler;

import com.henko.server.model.UniqueReq;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static io.netty.channel.ChannelHandler.*;

@Sharable
public class ServerConnectionCountHandler extends ChannelTrafficShapingHandler {

    /** Don`t execute doAccounting() method **/
    private static final int CHECK_INTERVAL = 0;

    private static final AtomicInteger CURRENT_CONNECTION_COUNT = new AtomicInteger();
    private static final Map<String, UniqueReq> UNIQUE_REQUESTS = new HashMap<>();


    private ServerConnectionCountHandler(long checkInterval) {
        super(checkInterval);
    }

    public ServerConnectionCountHandler() {
        this(CHECK_INTERVAL);
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        String ip = _parseIp(ctx);
        long connTime = System.currentTimeMillis();

        CURRENT_CONNECTION_COUNT.incrementAndGet();
        _addOrIncrementRequest(new UniqueReq(ip, connTime));

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

    public synchronized static int getCountOfAllReq() {
        int count = 0;
        for (UniqueReq req : UNIQUE_REQUESTS.values()) {
            count += req.getCount();
        }

        return count;
    }

    public static List<UniqueReq> getNUniqueReq() {
        List<UniqueReq> requests = new ArrayList<>();
        for (UniqueReq request : UNIQUE_REQUESTS.values()) {
            requests.add(request);
        }

        return requests;
    }

    public static int getNumOfUniqueReq() {
        return UNIQUE_REQUESTS.size();
    }

    private static synchronized void _addOrIncrementRequest(UniqueReq request) {
        String ip = request.getIp();
        long lastConn = request.getLastConn();

        if (UNIQUE_REQUESTS.containsKey(ip)) {
            int count = _getCountByIp(ip);
            UNIQUE_REQUESTS.put(ip, new UniqueReq(ip, ++count, lastConn));
        } else {
            UNIQUE_REQUESTS.put(ip, new UniqueReq(ip, 1, lastConn));
        }
    }

    private static int _getCountByIp(String ip) {
        return UNIQUE_REQUESTS.get(ip).getCount();
    }

    private String _parseIp(ChannelHandlerContext ctx) {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        return inetSocketAddress.getAddress().getHostAddress();
    }
}
