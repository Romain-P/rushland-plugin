package org.rushland.api.interfaces.database;

/**
 * Created by Return on 03/09/2014.
 * Data Access Object
 */
public interface DAO<T> {
    boolean create(T obj);
    boolean delete(T obj);
    boolean update(T obj);

    T load(Object primary);
}