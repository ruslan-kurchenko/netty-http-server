package com.henko.server.dao;

import com.henko.server.dao.exception.PersistException;

import java.util.List;

public interface GenericDao<T>{
    public T persist(T object) throws PersistException;
    
    public T selectById(int id) throws PersistException;

    public List<T> selectAll() throws PersistException;
}
