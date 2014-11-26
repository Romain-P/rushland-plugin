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
import org.rushland.plugin.entities.Item;
import org.rushland.plugin.entities.ItemBag;
import org.rushland.plugin.enums.GameType;
import org.rushland.plugin.enums.PluginType;
import org.rushland.plugin.games.entities.GameTypeProperty;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Managed by romain on 29/10/2014.
 */
@Getter
public class PluginFactory {
    private String name;
    private final PluginType type;
    private final long slots;
    private final String[] lobbyNames;
    private final String pvpName, mainName;
    private final Map<String, Client> clients;
    private final Map<Integer, Grade> grades;
    private final Map<Integer, Item> items;
    private final Map<Integer, ItemBag> itemBags;
    private final Map<String, GameType> gameTypes;
    private final Map<String, GameTypeProperty> gameTypeProperties;
    private final YamlConfiguration mainConfig, bungeeConfig;
    private DaoQueryManager<Client> clientManager;
    private DlaoQueryManager gradeLoader;

    @Inject
    DatabaseService database;

    @Inject
    public PluginFactory(JavaPlugin plugin) {
        this.clients = new HashMap<>();
        this.grades = new HashMap<>();
        this.gameTypeProperties = new HashMap<>();
        this.items    = new HashMap<>();
        this.itemBags = new HashMap<>();
        this.gameTypes = new HashMap<>();

        this.type = PluginType.get(plugin.getConfig().getString("plugin.type"));
        this.mainConfig = YamlConfiguration.loadConfiguration(new File(plugin.getConfig().getString("config.path")));
        this.bungeeConfig = YamlConfiguration.loadConfiguration(new File(mainConfig.getString("bungee.config-path")));

        this.slots = mainConfig.getInt("bungee.lobby-slots");

        List<String> list = new ArrayList<>();

        for(String name: bungeeConfig.getConfigurationSection("servers").getKeys(false)) {
            if(plugin.getServer().getPort() == Integer.parseInt(bungeeConfig.getString("servers."+name+".address").split(":")[1]))
                this.name = name;
            if (!name.equalsIgnoreCase("main") && !name.equalsIgnoreCase("pvp"))
                list.add(name);
        }

        this.lobbyNames = list.toArray(new String[1]);
        this.pvpName = "pvp";
        this.mainName = "main";

        for(String type: mainConfig.getConfigurationSection("gametypes").getKeys(false)) {
            String board = mainConfig.getString(String.format("gametypes.%s.name", type));
            this.gameTypeProperties.put(board, new GameTypeProperty(board,
                    mainConfig.getString(String.format("gametypes.%s.map.path", type)),
                    mainConfig.getString(String.format("gametypes.%s.wmap.path", type)),
                    mainConfig.getStringList(String.format("gametypes.%s.map.spawns", type)).toArray(new String[1]),
                    mainConfig.getStringList(String.format("gametypes.%s.wmap.spawns", type)).toArray(new String[1])));
        }
    }

    public void configure() {
        this.clientManager = (DaoQueryManager<Client>) database.getQueryManagers().get(ClientManager.class);
        this.gradeLoader = database.getLoadManagers().get(GradeLoader.class);
    }
}
