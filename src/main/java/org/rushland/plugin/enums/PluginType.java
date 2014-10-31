package org.rushland.plugin.enums;

/**
 * Managed by romain on 30/10/2014.
 */
public enum PluginType {
    MAIN,
    LOBBY,
    PVP;

    public static PluginType get(String name) {
        return valueOf(name.toUpperCase());
    }
}
