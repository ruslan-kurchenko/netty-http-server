package com.henko.server.dao.impl;


import com.henko.server.dao.ConnectionInfoDao;
import com.henko.server.dao.RedirectInfoDao;

public abstract class DaoFactory {
    
    public static final String H2 = "h2";
    
    public abstract ConnectionInfoDao getConnectionInfoDao();
    public abstract RedirectInfoDao getRedirectInfoDao();
    
    public static DaoFactory getDaoFactory(String type) {
        switch (type) {
            case H2: return new H2DaoFactory();
            default: return null;
        }
    }
}
