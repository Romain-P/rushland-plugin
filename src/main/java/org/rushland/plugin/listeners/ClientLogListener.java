package org.rushland.plugin.listeners;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.rushland.api.interfaces.bukkit.ImprovedListener;
import org.rushland.api.interfaces.games.GameMod;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.enums.BoardState;
import org.rushland.plugin.network.PluginNetworkService;

import java.util.Set;

/**
 * Managed by romain on 29/10/2014.
 */
public class ClientLogListener extends ImprovedListener {
    @Inject
    PluginFactory factory;
    @Inject
    Set<ImprovedListener> listeners;
    @Inject
    PluginNetworkService network;
    @Inject
    Injector injector;

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        Player player = get(event);
        System.out.println(event.getPlayer().getName() + ": : :"+player.getUniqueId().toString());
        Client client = factory.getClientManager().load(player.getUniqueId().toString());

        boolean inject;

        if((inject = client == null))
            client = new Client(player.getUniqueId().toString());

        injector.injectMembers(client);
        if(inject) client.create();


        factory.getClients().put(client.getUuid(), client);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = get(event);
        Client client = factory.getClients().get(player.getUniqueId().toString());

        for(ImprovedListener listener: listeners)
            listener.pullPlayer(player);

        if(client.getGameProfile() != null) {
            GameMod game = client.getGameProfile().getGame();
            boolean started = game.getState() == BoardState.FULL;
            network.sendMessage(player, factory.getMainName(), "disconnected" + (started ? "" : ":available"));
            game.delClient(client);
        }

        client.save();
        factory.getClients().remove(client.getUuid());
    }
}