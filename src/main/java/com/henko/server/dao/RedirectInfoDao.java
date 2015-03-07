package com.henko.server.dao;

import com.henko.server.model.RedirectInfo;

import java.util.List;

public interface RedirectInfoDao {

    public RedirectInfo selectById(int id);

    public RedirectInfo selectByUrl(String url);

    public int persist(String url);

    public boolean increaseCountByUrl(String url);

    public boolean updateCountByUrl(String url, int count);

    public List<RedirectInfo> selectAll();
}
