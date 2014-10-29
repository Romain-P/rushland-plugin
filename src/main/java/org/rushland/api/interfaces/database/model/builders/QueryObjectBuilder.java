package org.rushland.api.interfaces.database.model.builders;

import org.rushland.api.interfaces.database.model.Query;
import org.rushland.api.interfaces.database.model.QueryModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Return on 06/09/2014.
 */
public class QueryObjectBuilder {
    public static Query newQuery(QueryModel<?> model, ResultSet result) throws SQLException {
        Query query = model.createNewQuery();

        if(result.next())
            for(String column: model.getColumns().keySet())
                query.getData().put(column, result.getObject(column));

        return query;
    }

    public static List newQueries(QueryModel<?> model, ResultSet result) throws SQLException {
        List<Query> list = new ArrayList<>();
        while(result.next()) {
            Query query = model.createNewQuery();
            for (String column : model.getColumns().keySet())
                query.getData().put(column, result.getObject(column));
            list.add(query);
        }
        return list;
    }
}
