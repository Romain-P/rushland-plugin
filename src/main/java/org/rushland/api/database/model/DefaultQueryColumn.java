package org.rushland.api.database.model;

import lombok.Getter;
import org.rushland.api.interfaces.database.model.QueryColumn;

/**
 * Created by Return on 03/09/2014.
 */
public class DefaultQueryColumn implements QueryColumn {
    private final String fieldName;
    @Getter
    private final String columnName;
    @Getter
    private final Class type;

    public DefaultQueryColumn(String fieldName, String columnName, Class type) {
        this.fieldName = fieldName;
        this.columnName = columnName;
        this.type = type;
    }
}
