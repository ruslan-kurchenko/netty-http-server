package com.henko.server.dao;

import com.henko.server.dao.exception.PersistException;
import com.henko.server.model.Redirect;

import java.util.List;

public interface RedirectDao {

    public Redirect getByUrl(String url);

    public List<Redirect> getAll();

    public List<Redirect> getNRedirect(int amount);

    public void addOrIncrementCount(String url) throws PersistException;
}
