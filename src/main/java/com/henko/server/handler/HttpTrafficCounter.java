package com.henko.server.handler;

import com.henko.server.dao.ConnectionInfoDao;
import com.henko.server.dao.impl.DaoFactory;
import com.henko.server.model.ConnectionInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;

import java.util.Date;

import static com.henko.server.dao.impl.DaoFactory.*;

public class HttpTrafficCounter extends ChannelTrafficShapingHandler {

    private ConnectionInfoDao connInfoDao;
    private ConnectionInfo connInfo;

    private double durationMillis;
    private long receivedBytes;
    private long sendBytes;
    private long speed;

    private HttpTrafficCounter(long checkInterval) {
        super(checkInterval);
    }

    public HttpTrafficCounter(long checkInterval, ConnectionInfo connInfo) {
        this(checkInterval);

        this.connInfoDao = getDaoFactory(H2).getConnectionInfoDao();
        this.connInfo = connInfo;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        long startConnTimeStamp = System.currentTimeMillis();
        connInfo.setTimestamp(startConnTimeStamp);

        super.handlerAdded(ctx);
    }

    @Override
    public synchronized void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);

        saveConnInfoData();
    }

    private void saveConnInfoData() {
        long stopConnTimeStamp = System.currentTimeMillis();
        durationMillis = parseDurationMillis(stopConnTimeStamp);

        receivedBytes = this.trafficCounter().cumulativeReadBytes();
        sendBytes = this.trafficCounter().cumulativeWrittenBytes();
        speed = parseSpeed();

        connInfo.setReceivedBytes(receivedBytes);
        connInfo.setSendBytes(sendBytes);
        connInfo.setSpeed(speed);

        connInfoDao.insertConnectionInfo(connInfo);

        System.err.println("\nstart - " + connInfo.getTimestamp() + ", end - " + stopConnTimeStamp + ", duration - " + durationMillis);
        System.err.println("handler removed: duration: - " + durationMillis + ", received_b - " + receivedBytes +
                ", send_b - " + sendBytes +
                ", speed - " + speed + "\n");
    }

    private long parseDurationMillis(long stopConnTimeStamp) {
        long result = stopConnTimeStamp - connInfo.getTimestamp();

        if (result == 0) return 1;

        return result;
    }

    private long parseSpeed() {
        double durationSec = durationMillis / 1000.0;

        return (long) ((receivedBytes + sendBytes) / durationSec);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }
}
