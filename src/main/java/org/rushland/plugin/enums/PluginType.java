package org.rushland.plugin.enums;

/**
 * Managed by romain on 30/10/2014.
 */
public enum PluginType {
    MAIN,
    LOBBY,
    PVP;

    public static PluginType get(String name) {
        switch(name) {
            case "main":
                return MAIN;
            case "pvp":
                return PVP;
            case "lobby":
            default:
                return LOBBY;
        }
    }
}
