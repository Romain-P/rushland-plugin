package org.rushland.api.interfaces.database;

import org.rushland.api.interfaces.database.model.Query;

import java.sql.SQLException;

/**
 * Managed by romain on 30/09/2014.
 */
public abstract class DlaoQueryManager implements DLAO{
    protected abstract Query[] createNewQueries() throws SQLException;
}
