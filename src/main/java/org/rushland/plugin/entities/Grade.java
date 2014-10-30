package org.rushland.plugin.entities;

import lombok.Getter;
import org.rushland.api.interfaces.database.model.annotations.PrimaryQueryField;
import org.rushland.api.interfaces.database.model.annotations.QueryField;

/**
 * Managed by romain on 30/10/2014.
 */
public class Grade {
    @PrimaryQueryField
    private final int id;
    @Getter
    @QueryField
    private final String prefix;

    public Grade() {
        this(0, null);
    }

    public Grade(int id, String prefix) {
        this.id = id;
        this.prefix = prefix;
    }
}
