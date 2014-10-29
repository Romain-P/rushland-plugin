package org.rushland.api.interfaces.database;

import org.rushland.api.interfaces.database.model.Query;
import org.rushland.api.interfaces.database.model.QueryModel;
import org.rushland.api.interfaces.database.model.enums.OnlyExecuteQueryEnum;

import java.sql.SQLException;

/**
 * Created by Return on 05/09/2014.
 */
public abstract class DaoQueryManager<T> implements DAO<T>{
    protected abstract void execute(QueryModel model, Object primary, OnlyExecuteQueryEnum type) throws SQLException;
    protected abstract void execute(Query query, OnlyExecuteQueryEnum type) throws SQLException;
    protected abstract Query createNewQuery(Object primary) throws SQLException;
}
