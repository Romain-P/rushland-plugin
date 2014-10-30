package org.rushland.core;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.interfaces.database.DatabaseService;
import org.rushland.database.DatabaseModule;
import org.rushland.plugin.PluginModule;

import java.sql.SQLException;
import java.util.Set;

/**
 * Managed by romain on 29/10/2014.
 */
public class Main extends JavaPlugin {
    @Inject
    DatabaseService database;
    @Inject
    Set<Listener> listeners;

    public void onEnable() {
        getLogger().info("creating guice injector..");
        Guice.createInjector(new CoreModule(this), new DatabaseModule(), new PluginModule());

        getLogger().info("starting database..");
        try {
            database.start();
        } catch (SQLException e) {
            getLogger().warning(String.format("error when trying to start the database: %s", e.getMessage()));
            getLogger().warning("the server can't start.. stopping the server..");
            getServer().dispatchCommand(this.getServer().getConsoleSender(), "save-all");
            getServer().dispatchCommand(this.getServer().getConsoleSender(), "stop");
            return;
        }

        getLogger().info("registering listeners");
        for(Listener listener: listeners)
            getServer().getPluginManager().registerEvents(listener, this);

        getLogger().info("plugin is now enabled!");
    }

    public void onDisable() {
        getLogger().info("stopping database..");
        database.stop();
    }
}
