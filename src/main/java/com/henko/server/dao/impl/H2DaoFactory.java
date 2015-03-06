package com.henko.server.dao.impl;

import com.henko.server.dao.GenericDao;
import com.henko.server.model.ConnectionInfo;

public class H2DaoFactory extends DaoFactory {

    @Override
    public GenericDao<ConnectionInfo> getConnectionInfoDao() {
        return null;
    }
    
}
