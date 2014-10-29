package org.rushland.plugin;

import lombok.Getter;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.entities.Grade;

import java.util.HashMap;
import java.util.Map;

/**
 * Managed by romain on 29/10/2014.
 */
@Getter
public class PluginFactory {
    public final Map<String, Client> clients;
    public final Map<Integer, Grade> grades;

    public PluginFactory() {
        this.clients = new HashMap<>();
        this.grades = new HashMap<>();
    }
}
