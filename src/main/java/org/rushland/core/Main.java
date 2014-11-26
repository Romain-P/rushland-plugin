package org.rushland.core;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.interfaces.bukkit.ImprovedListener;
import org.rushland.api.interfaces.database.DatabaseService;
import org.rushland.database.DatabaseModule;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.PluginModule;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.network.PluginNetworkService;

import java.sql.SQLException;
import java.util.Set;

/**
 * Managed by romain on 29/10/2014.
 */
public class Main extends JavaPlugin {
    @Inject
    DatabaseService database;
    @Inject
    Set<ImprovedListener> listeners;
    @Inject
    PluginNetworkService network;
    @Inject
    PluginFactory factory;

    public void onEnable() {
        getLogger().info("creating guice injector...");
        Guice.createInjector(new CoreModule(this), new DatabaseModule(), new PluginModule());

        getLogger().info("starting database..");
        try {
            database.start();
        } catch (SQLException e) {
            getLogger().warning(String.format("error when trying to start the database: %s", e.getMessage()));
            getLogger().warning("the server can't start.. stopping the server..");
            getServer().dispatchCommand(this.getServer().getConsoleSender(), "save-all");
            getServer().dispatchCommand(this.getServer().getConsoleSender(), "end");
            return;
        }

        getLogger().info("configuring the plugin factory..");
        factory.configure();

        getLogger().info("registering listeners..");
        for(Listener listener: listeners)
            getServer().getPluginManager().registerEvents(listener, this);

        getLogger().info("enabling communication's network..");

        try {
            network.start();
        } catch(Exception e) {
            getLogger().warning(String.format("error when trying to start the network: %s", e.getMessage()));
        }

        getLogger().info("plugin is now enabled!");
    }

    public void onDisable() {
        getLogger().info("saving players..");
        for(Client client: factory.getClients().values())
            client.save();

        getLogger().info("stopping database..");
        database.stop();

        getLogger().info("unregistering listeners..");
        HandlerList.unregisterAll(this);

        getLogger().info("disabling network");
        network.stop();

        getLogger().info("plugin is now disabled");
    }
}
