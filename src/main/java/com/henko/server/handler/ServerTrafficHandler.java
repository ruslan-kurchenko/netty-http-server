package com.henko.server.handler;

import com.henko.server.dao.ConnectDao;
import com.henko.server.model.Connect;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;

import static com.henko.server.dao.impl.DaoFactory.*;

public class ServerTrafficHandler extends ChannelTrafficShapingHandler {

    private ConnectDao connInfoDao;
    private Connect connInfo;

    private double durationMillis;
    private long receivedBytes;
    private long sendBytes;

    private ServerTrafficHandler(long checkInterval) {
        super(checkInterval);
    }

    public ServerTrafficHandler(long checkInterval, Connect connInfo) {
        this(checkInterval);

        this.connInfoDao = getDaoFactory(H2).getConnectionDao();
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
        long speed = parseSpeed();

        connInfo.setReceivedBytes(receivedBytes);
        connInfo.setSendBytes(sendBytes);
        connInfo.setSpeed(speed);

        connInfoDao.insertConnectionInfo(connInfo);

//        System.err.println("\nstart - " + connInfo.getTimestamp() + ", end - " + stopConnTimeStamp + ", duration - " + durationMillis);
//        System.err.println("handler removed: duration: - " + durationMillis + ", received_b - " + receivedBytes +
//                ", send_b - " + sendBytes +
//                ", speed - " + speed + "\n");
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
        super.exceptionCaught(ctx, cause);
    }
}
