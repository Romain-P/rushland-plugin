package org.rushland.database;

import com.google.inject.Inject;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.interfaces.database.DaoQueryManager;
import org.rushland.api.interfaces.database.DatabaseService;
import org.rushland.api.interfaces.database.DlaoQueryManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Managed by romain on 29/10/2014.
 */
public class PluginDatabaseService implements DatabaseService{
    @Getter
    private final ReentrantLock locker;
    @Getter
    private Connection connection;
    @Getter
    private Map<Class, DaoQueryManager<?>> queryManagers;
    @Getter
    private Map<Class, DlaoQueryManager> loadManagers;

    @Inject
    Set<DaoQueryManager> managers;
    @Inject
    Set<DlaoQueryManager> lManagers;
    @Inject
    JavaPlugin plugin;

    public PluginDatabaseService() {
        this.locker = new ReentrantLock();
        this.queryManagers = new HashMap<>();
        this.loadManagers = new HashMap<>();
    }

    public PluginDatabaseService start() throws SQLException {
        FileConfiguration config = plugin.getConfig();
        connection = DriverManager.getConnection("jdbc:mysql://" +
                        config.getString("database.host") + "/" +
                        config.getString("database.name"),
                        config.getString("database.user"),
                        config.getString("database.pass"));

        if (!connection.isValid(1000)) return null;
        connection.setAutoCommit(true);

        plugin.getLogger().info("loading static data..");
        for(DlaoQueryManager manager: lManagers) {
            loadManagers.put(manager.getClass(), manager);
            manager.loadAll();
        }

        plugin.getLogger().info("static data loaded!");
        for(DaoQueryManager manager: managers)
            queryManagers.put(manager.getClass(), manager);

        return this;
    }

    public void stop() {
        try {
            if(connection != null && !connection.isClosed())
                connection.close();
        } catch(SQLException exception){}
    }
}
