package org.rushland.plugin;

import lombok.Getter;
import org.rushland.plugin.entities.Client;

import java.util.HashMap;
import java.util.Map;

/**
 * Managed by romain on 29/10/2014.
 */
@Getter
public class PluginFactory {
    public final Map<String, Client> clients;

    public PluginFactory() {
        this.clients = new HashMap<>();
    }
}
