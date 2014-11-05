package org.rushland.plugin;

import com.google.inject.Inject;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.interfaces.database.DaoQueryManager;
import org.rushland.api.interfaces.database.DatabaseService;
import org.rushland.api.interfaces.database.DlaoQueryManager;
import org.rushland.database.tables.ClientManager;
import org.rushland.database.tables.GradeLoader;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.entities.Grade;
import org.rushland.plugin.enums.GameType;
import org.rushland.plugin.enums.PluginType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Managed by romain on 29/10/2014.
 */
@Getter
public class PluginFactory {
    private final PluginType type;
    private final long slots;
    private final String[] lobbyNames;
    private final String pvpName, mainName;
    private final Map<String, Client> clients;
    private final Map<Integer, Grade> grades;
    private final Map<String, GameType> gameTypes;
    private final YamlConfiguration mainConfig;
    private DaoQueryManager<Client> clientManager;
    private DlaoQueryManager gradeLoader;

    @Inject
    DatabaseService database;

    @Inject
    public PluginFactory(JavaPlugin plugin) {
        this.type = PluginType.get(plugin.getConfig().getString("plugin.type"));

        this.mainConfig = YamlConfiguration.loadConfiguration(new File(plugin.getConfig().getString("config.path")));
        this.lobbyNames = mainConfig.getStringList("server-names.lobbies").toArray(new String[] {});
        this.pvpName = mainConfig.getString("server-names.pvp");
        this.mainName = mainConfig.getString("server-names.main");
        this.slots = mainConfig.getInt("lobby.slots");

        this.clients = new HashMap<>();
        this.grades = new HashMap<>();
        this.gameTypes = new HashMap<>();

        this.gameTypes.put(mainConfig.getString("gametype-names.rush"), GameType.RUSH);
        this.gameTypes.put(mainConfig.getString("gametype-names.antwar"), GameType.ANTWAR);
        this.gameTypes.put(mainConfig.getString("gametype-names.devided"), GameType.DIVIDED_TOGETHER);
        this.gameTypes.put(mainConfig.getString("gametype-names.tower"), GameType.TOWER);
    }

    public void configure() {
        this.clientManager = (DaoQueryManager<Client>) database.getQueryManagers().get(ClientManager.class);
        this.gradeLoader = database.getLoadManagers().get(GradeLoader.class);
    }
}
