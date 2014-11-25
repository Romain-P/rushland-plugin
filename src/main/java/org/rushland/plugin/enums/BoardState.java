package org.rushland.plugin.enums;

/**
 * Managed by romain on 22/11/2014.
 */
public enum BoardState {
    AVAILABLE("Disponible"),
    STARTING("Lancement.."),
    FULL("Complet");

    private String value;
    private BoardState(String value) {
        this.value = value;
    }

    public String get() {
        return this.value;
    }
}
