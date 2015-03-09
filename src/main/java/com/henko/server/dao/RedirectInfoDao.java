package com.henko.server.dao;

import com.henko.server.dao.exception.PersistException;
import com.henko.server.model.RedirectInfo;

import java.util.List;

public interface RedirectInfoDao {

    public RedirectInfo selectByUrl(String url);

    public List<RedirectInfo> selectAll();

    public void addOrIncrementCount(String url) throws PersistException;

    public List<RedirectInfo> selectListByMaxCount(int howMany);
}
