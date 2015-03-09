package com.henko.server.dao.impl;


import com.henko.server.dao.ConnectDao;
import com.henko.server.dao.RedirectDao;

public abstract class DaoFactory {
    
    public static final String H2 = "h2";
    
    public abstract ConnectDao getConnectionDao();
    public abstract RedirectDao getRedirectDao();
    
    public static DaoFactory getDaoFactory(String type) {
        switch (type) {
            case H2: return new H2DaoFactory();
            default: return null;
        }
    }
}
