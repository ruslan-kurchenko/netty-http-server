package com.henko.server.dao.impl;

import com.henko.server.dao.ConnectionInfoDao;
import com.henko.server.dao.RedirectInfoDao;

public class H2DaoFactory extends DaoFactory {

    @Override
    public ConnectionInfoDao getConnectionInfoDao() {
        return new H2ConnectionInfoDao();
    }

    @Override
    public RedirectInfoDao getRedirectInfoDao() {
        return new H2RedirectInfoDao();
    }
}
