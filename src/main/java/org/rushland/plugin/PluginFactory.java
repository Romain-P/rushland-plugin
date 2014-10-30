package org.rushland.plugin;

import com.google.inject.Inject;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.entities.Grade;

import java.util.HashMap;
import java.util.Map;

/**
 * Managed by romain on 29/10/2014.
 */
@Getter
public class PluginFactory {
    public final boolean main;
    public final long slots;
    public final Map<String, Client> clients;
    public final Map<Integer, Grade> grades;

    @Inject
    public PluginFactory(JavaPlugin plugin) {
        this.main = plugin.getConfig().getBoolean("plugin.main");
        this.slots = plugin.getConfig().getLong("plugin.slots");
        this.clients = new HashMap<>();
        this.grades = new HashMap<>();
    }
}
