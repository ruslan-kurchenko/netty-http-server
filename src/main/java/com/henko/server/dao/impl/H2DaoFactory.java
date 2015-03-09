package com.henko.server.dao.impl;

import com.henko.server.dao.ConnectDao;
import com.henko.server.dao.RedirectDao;

public class H2DaoFactory extends DaoFactory {

    @Override
    public ConnectDao getConnectionDao() {
        return new H2ConnectDao();
    }

    @Override
    public RedirectDao getRedirectDao() {
        return new H2RedirectDao();
    }
}
