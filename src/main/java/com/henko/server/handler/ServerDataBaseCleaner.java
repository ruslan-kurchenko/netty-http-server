package com.henko.server.handler;


import com.henko.server.db.DBManager;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;

public class ServerDataBaseCleaner extends ChannelTrafficShapingHandler {

    private final int _leftRows;

    private final static int DEFAULT_LEFT_ROWS = 500;
    private final static int DEFAULT_CLEAN_INTERVAL = 1000;

    private final DBManager _dbManager = new DBManager();

    public ServerDataBaseCleaner() {
        super(DEFAULT_CLEAN_INTERVAL);
        this._leftRows = DEFAULT_LEFT_ROWS;
    }

    public ServerDataBaseCleaner(long cleanInterval) {
        super(cleanInterval);
        this._leftRows = DEFAULT_LEFT_ROWS;
    }

    public ServerDataBaseCleaner(long cleanInterval, int leftRows) {
        super(cleanInterval);
        this._leftRows = leftRows;
    }

    @Override
    protected void doAccounting(TrafficCounter counter) {
        _dbManager.cleanConnectionInfoTable(_leftRows);

        super.doAccounting(counter);
    }
}
