package org.rushland.plugin.network;

import com.google.inject.Inject;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.enums.PluginType;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

/**
 * Managed by romain on 31/10/2014.
 */
public class PluginNetworkHandler implements PluginMessageListener {
    @Inject
    PluginFactory factory;
    @Inject
    PluginNetworkService network;

    @SneakyThrows
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        DataInput in = new DataInputStream(new ByteArrayInputStream(bytes));

        if(!in.readUTF().equalsIgnoreCase("rushland")) return;

        parse(player, new DataInputStream(new ByteArrayInputStream(new byte[in.readShort()])).readUTF());
    }

    private void parse(Player player, String msg) {
        Client client = factory.getClients().get(player.getUniqueId().toString());

        switch(msg.toLowerCase()) {
            case "disconnected": {
                if(factory.getType() == PluginType.MAIN)
                    for(String server: client.getCachedLobbies())
                        network.sendMessage(player, server, "disconnected");
                factory.getClients().remove(client.getUuid());
            }
        }
    }
}
