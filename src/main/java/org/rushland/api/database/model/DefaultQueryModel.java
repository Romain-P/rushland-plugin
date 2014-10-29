package org.rushland.api.database.model;

import lombok.Getter;
import org.rushland.api.interfaces.database.model.Query;
import org.rushland.api.interfaces.database.model.QueryColumn;
import org.rushland.api.interfaces.database.model.QueryModel;
import org.rushland.api.interfaces.database.model.annotations.PrimaryQueryField;
import org.rushland.api.interfaces.database.model.annotations.QueryField;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Return on 03/09/2014.
 */
public class DefaultQueryModel<T> implements QueryModel<T> {
    @Getter
    private final String tableName;
    @Getter
    private String primaryKeyName;
    private final T schema;
    @Getter
    private final Map<String, QueryColumn> columns;

    public DefaultQueryModel(String tableName, T schema) {
        this.tableName = tableName;
        this.schema = schema;
        this.columns = new HashMap<>();
    }

    public Map<String, String> getColumnModel() {
        return new HashMap<>();
    }

    public QueryModel<T> schematize() {
        boolean defaultModel = getColumnModel().isEmpty();

        for(Field field: schema.getClass().getDeclaredFields()) {
            boolean primary = false;

            if(field.isAnnotationPresent(PrimaryQueryField.class)) {
                String name = field.getName();
                primaryKeyName = name;
                if(defaultModel) getColumnModel().put(name, name);
                primary = true;
            }

            if(field.isAnnotationPresent(QueryField.class) || primary) {
                String fieldName = field.getName();
                if(defaultModel) getColumnModel().put(fieldName, fieldName);

                columns.put(fieldName, new DefaultQueryColumn(fieldName, defaultModel ? fieldName: getColumnModel().get(fieldName), field.getType()));
            }
        }
        return this;
    }

    public Query createNewQuery() {
        Query query = new DefaultQuery(this);
        return query;
    }
}
