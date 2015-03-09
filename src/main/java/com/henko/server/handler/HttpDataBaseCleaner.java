package com.henko.server.handler;


import com.henko.server.db.DBManager;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;

public class HttpDataBaseCleaner extends ChannelTrafficShapingHandler {

    private final int _leftRows;

    private final static int DEFAULT_LEFT_ROWS = 500;
    private final static int DEFAULT_CLEAN_INTERVAL = 1000;

    private final DBManager dbManager = new DBManager();

    public HttpDataBaseCleaner() {
        super(DEFAULT_CLEAN_INTERVAL);
        this._leftRows = DEFAULT_LEFT_ROWS;
    }

    public HttpDataBaseCleaner(long cleanInterval) {
        super(cleanInterval);
        this._leftRows = DEFAULT_LEFT_ROWS;
    }

    public HttpDataBaseCleaner(long cleanInterval, int leftRows) {
        super(cleanInterval);
        this._leftRows = leftRows;
    }

    @Override
    protected void doAccounting(TrafficCounter counter) {
        dbManager.cleanConnectionInfoTable(_leftRows);

        super.doAccounting(counter);
    }
}
