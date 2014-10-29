package org.rushland.core;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.interfaces.database.DatabaseService;
import org.rushland.database.DatabaseModule;

import java.sql.SQLException;

/**
 * Managed by romain on 29/10/2014.
 */
public class Main extends JavaPlugin {
    @Inject
    DatabaseService database;

    public void onEnable() {
        Guice.createInjector(new CoreModule(this), new DatabaseModule());

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
    }

    public void onDisable() {
        getLogger().info("stopping database..");
        database.stop();
    }
}
