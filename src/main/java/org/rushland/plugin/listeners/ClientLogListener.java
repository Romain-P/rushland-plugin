package org.rushland.plugin.listeners;

import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.rushland.api.interfaces.bukkit.ImprovedListener;
import org.rushland.api.interfaces.database.DaoQueryManager;
import org.rushland.api.interfaces.database.DatabaseService;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.enums.PluginType;
import org.rushland.plugin.network.PluginNetworkService;

import java.util.Set;

/**
 * Managed by romain on 29/10/2014.
 */
public class ClientLogListener extends ImprovedListener {
    private final DaoQueryManager<Client> manager;

    @Inject
    PluginFactory factory;
    @Inject
    Set<ImprovedListener> listeners;
    @Inject
    PluginNetworkService network;

    @Inject
    public ClientLogListener(DatabaseService database) {
        this.manager = (DaoQueryManager<Client>) database.getQueryManagers().get(Client.class);
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        Player player = get(event);
        Client client = manager.load(player.getUniqueId());

        if(client == null)
            client = new Client(player.getUniqueId().toString()).create();

        factory.getClients().put(player.getUniqueId().toString(), client);
        player.sendMessage("Bienvenue sur Rushland");
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = get(event);
        Client client = factory.getClients().get(player.getUniqueId().toString());

        for(ImprovedListener listener: listeners)
            listener.pullPlayer(player);

        if(factory.getType() == PluginType.MAIN) {
            /** clearing the cache **/
            for(String server: client.getCachedLobbies())
                network.sendMessage(player, server, "disconnected");

            factory.getClients().remove(client.getUuid());
        } else
            network.sendMessage(player, factory.getMainName(), "disconnected");

        client.save();
    }
}