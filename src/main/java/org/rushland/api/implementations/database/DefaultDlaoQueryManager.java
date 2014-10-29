package org.rushland.api.implementations.database;

import com.google.inject.Inject;
import org.rushland.api.interfaces.database.DatabaseService;
import org.rushland.api.interfaces.database.DlaoQueryManager;
import org.rushland.api.interfaces.database.model.Query;
import org.rushland.api.interfaces.database.model.QueryModel;
import org.rushland.api.interfaces.database.model.builders.PreparedStatementBuilder;
import org.rushland.api.interfaces.database.model.builders.QueryObjectBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Managed by romain on 30/09/2014.
 */
public abstract class DefaultDlaoQueryManager extends DlaoQueryManager {
    protected QueryModel model;
    @Inject
    DatabaseService database;

    public DefaultDlaoQueryManager(QueryModel model) {
        this.model = model;
    }

    protected Query[] createNewQueries() throws SQLException {
        database.getLocker().lock();

        ResultSet resultSet;
        ArrayList<Query> queries = new ArrayList<>();
        try {
            database.getConnection().setAutoCommit(false);
            PreparedStatement statement = PreparedStatementBuilder.newLoadQuery(model, database.getConnection());
            resultSet = statement.executeQuery();
            database.getConnection().commit();
            queries.addAll(QueryObjectBuilder.newQueries(model, resultSet));

            resultSet.getStatement().close();
            resultSet.close();
        } catch(SQLException exception) {
            throw exception;
        } finally {
            database.getConnection().setAutoCommit(true);
            database.getLocker().unlock();
        }
        return queries.toArray(new Query[] {});
    }
}
