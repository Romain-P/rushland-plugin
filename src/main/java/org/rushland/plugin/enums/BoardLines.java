package org.rushland.plugin.enums;

/**
 * Managed by romain on 31/10/2014.
 */
public enum BoardLines {
    GAME_TYPE(0),
    STATE(1),
    PLAYERS(2),
    NAME(3);

    private int line;
    private BoardLines(int line) {
        this.line = line;
    }

    public int get() {
        return this.line;
    }
}