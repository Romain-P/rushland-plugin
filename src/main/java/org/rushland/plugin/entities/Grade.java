package org.rushland.plugin.entities;

import lombok.Getter;

/**
 * Managed by romain on 30/10/2014.
 */
public class Grade {
    private final int id;
    @Getter
    private final String prefix;

    public Grade() {
        this(0, null);
    }

    public Grade(int id, String prefix) {
        this.id = id;
        this.prefix = prefix;
    }
}
