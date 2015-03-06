package com.henko.server.dao.impl;

import com.henko.server.dao.GenericDao;
import com.henko.server.model.ConnectionInfo;

public abstract class DaoFactory {
    
    public static final String H2 = "h2";
    
    public abstract GenericDao<ConnectionInfo> getConnectionInfoDao();
    
    public static DaoFactory getDaoFactory(String type) {
        switch (type) {
            case H2: return new H2DaoFactory();
            default: return null;
        }
    }
}
