package org.rushland.plugin;

import com.google.inject.Inject;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.entities.Grade;
import org.rushland.plugin.entities.Item;
import org.rushland.plugin.enums.PluginType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Managed by romain on 29/10/2014.
 */
@Getter
public class PluginFactory {
    public final PluginType type;
    public final long slots;
    public final Map<String, Client> clients;
    public final Map<Integer, Grade> grades;
    public final Map<Integer, Item> items;

    @Inject
    public PluginFactory(JavaPlugin plugin) {
        this.type = PluginType.get(plugin.getConfig().getString("plugin.key"));
        this.slots = plugin.getConfig().getLong("plugin.slots");
        this.clients = new HashMap<>();
        this.grades = new HashMap<>();
        this.items = new HashMap<>();
    }

    /**
     * Generate a Bukkit Inventory containing all item given as ID.
     * @param title resulting inventory's title
     * @param ids all items' id which has be present in resulting inventory
     * @return a non-null inventory
     */
    public Inventory generateInventory(String title, List<Integer> ids) {
        Inventory result = Bukkit.createInventory(null, (ids.size() % 9 + 1) * 9, title);
        for (Integer id : ids) {
            Item item = items.get(id);
            result.addItem(item.generate());
        }
        return result;
    }
}
