package org.rushland.plugin.network;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.interfaces.network.NetworkConnector;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.enums.PluginType;
import org.rushland.plugin.games.GameManager;
import org.rushland.plugin.games.entities.GameTypeProperty;
import org.rushland.utils.TimeUtils;

/**
 * Managed by romain on 31/10/2014.
 */
public class PluginNetworkService extends NetworkConnector {
    @Inject
    JavaPlugin plugin;
    @Inject
    Injector injector;
    @Inject
    PluginFactory factory;
    @Inject
    GameManager gameManager;

    @Getter
    @Setter
    private IoSession network;

    @Override
    protected void configure() {
        IoHandler handler = new PluginNetworkHandler();
        injector.injectMembers(handler);

        connector.setHandler(handler);
    }

    public void dispatchTo(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    @SneakyThrows
    public void sendMessage(Player player, String server, String msg) {
        String packet = server+":"+player.getName()+":"+msg;
        network.write(packet);
    }

    public void parse(final String msg) {
        String[] split = msg.split(":");
        final String from = split[0];
        Client client = factory.getClients().get(from);

        switch(split[1].toLowerCase()) {
            case "disconnected": {
                gameManager.exit(from, split.length > 2 && split[2].equals("available"));
                break;
            }

            case "game": {
                if(factory.getType() != PluginType.MAIN) {
                    if(client == null) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            public void run() {
                                parse(msg);
                            }
                        }, TimeUtils.convertSecondsToTicks(0.5));
                        return;
                    }

                    String name = split[2];
                    GameTypeProperty property = factory.getGameTypeProperties().get(split[3]);
                    int maxClients = Integer.parseInt(split[4]);

                    gameManager.joinGame(client, name, property, maxClients);
                } else
                    gameManager.exit(from, false);
                break;
            }
        }
    }
}
