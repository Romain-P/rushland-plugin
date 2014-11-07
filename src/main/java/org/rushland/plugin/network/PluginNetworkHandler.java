package org.rushland.plugin.network;

import com.google.inject.Inject;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.rushland.api.interfaces.database.DaoQueryManager;
import org.rushland.api.interfaces.database.DatabaseService;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.enums.PluginType;
import org.rushland.plugin.games.GameManager;
import org.rushland.plugin.games.entities.GameTypeProperty;

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
    @Inject
    GameManager gameManager;
    @Inject
    DatabaseService database;

    @SneakyThrows
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        DataInput in = new DataInputStream(new ByteArrayInputStream(bytes));

        String subChannel = in.readUTF();
        String[] split = subChannel.split(":");
        if(!split[0].equalsIgnoreCase("rushland")) return;

        parse(player, split[1], new DataInputStream(new ByteArrayInputStream(new byte[in.readShort()])).readUTF());
    }

    private void parse(Player player, String from, String msg) {
        Client client = factory.getClients().get(player.getUniqueId().toString());
        DaoQueryManager<Client> manager = factory.getClientManager();

        String[] split = msg.split(":");
        switch(split[0].toLowerCase()) {
            case "disconnected": {
                client = manager.load(player.getUniqueId().toString());
                gameManager.exit(client);
                factory.getClients().remove(client.getUuid());
                break;
            }

            case "game": {
                if(factory.getType() == PluginType.MAIN) {
                    gameManager.exit(client);
                } else {
                    String name = split[1];
                    GameTypeProperty property = factory.getGameTypeProperties().get(split[2]);
                    int maxClients = Integer.parseInt(split[3]);

                    gameManager.joinGame(client, name, property, maxClients);
                }
                break;
            }
        }
    }
}
