package org.rushland.plugin;

import com.google.inject.Inject;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.entities.Grade;
import org.rushland.plugin.entities.Item;
import org.rushland.plugin.entities.ItemBag;
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
    private final Map<Integer, Item> items;
    private final Map<Integer, ItemBag> itemBags;

    @Inject
    public PluginFactory(JavaPlugin plugin) {
        this.type = PluginType.get(plugin.getConfig().getString("plugin.type"));
        this.slots = plugin.getConfig().getLong("plugin.slots");

        YamlConfiguration mainConfig = YamlConfiguration.loadConfiguration(new File(plugin.getConfig().getString("config.path")));
        this.lobbyNames = mainConfig.getStringList("server-names.lobbies").toArray(new String[] {});
        this.pvpName = mainConfig.getString("server-names.pvp");
        this.mainName = mainConfig.getString("server-names.main");

        this.clients  = new HashMap<>();
        this.grades   = new HashMap<>();
        this.items    = new HashMap<>();
        this.itemBags = new HashMap<>();
    }
}
