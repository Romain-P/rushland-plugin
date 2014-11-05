package org.rushland.api.implementations.database.model;

import lombok.Getter;
import org.rushland.api.interfaces.database.model.Query;
import org.rushland.api.interfaces.database.model.QueryModel;
import org.rushland.api.interfaces.database.model.exceptions.BadPutFieldTypeException;
import org.rushland.api.interfaces.database.model.exceptions.BadQueryFormationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Return on 03/09/2014.
 */
public class DefaultQuery implements Query {
    @Getter
    private final Map<String, Object> data;
    @Getter
    private final QueryModel<?> model;

    public DefaultQuery(QueryModel<?> model) {
        this.model = model;
        this.data = new HashMap<>();
    }

    public Query setData(String column, Object data) throws NullPointerException {
        Class type = model.getColumns().get(column).getType();
        Class datatype = data.getClass();

        if(datatype == Integer.class)
            datatype = int.class;
        else if (datatype == Double.class)
            datatype = double.class;
        else if (datatype == Long.class)
            datatype = long.class;

        if(type == null)
            throw new NullPointerException(String.format("QueryModel's column not found: %s", column));
        else if(type !=  datatype)
            throw new BadPutFieldTypeException(String.format("QueryModel %s (%s) needed type %s", data.getClass(), column, type));

        this.data.put(column, data);
        return this;
    }



    public boolean checkFormation() {
        int size = model.getColumns().size() - data.size();

        if(size != 0)
            throw new BadQueryFormationException(String.format("Created query %s cannot be used cause model size is different %d", model.getTableName(), size));

        return true;
    }
}
