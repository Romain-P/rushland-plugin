package org.rushland.plugin.network;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Managed by romain on 31/10/2014.
 */
public class PluginNetworkService {
    private final PluginMessageListener handler;

    @Inject
    JavaPlugin plugin;
    @Inject
    Injector injector;

    public PluginNetworkService() {
        injector.injectMembers((this.handler = new PluginNetworkHandler()));
    }

    public void start() {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", handler);
    }

    public void stop() {
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord", handler);
    }

    public void dispatchTo(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    public void sendMessage(Player player, String server, String msg) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF(server);
        out.writeUTF("rushland");

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);

        try {
            msgout.writeUTF(msg);
        } catch (IOException e) {
            plugin.getLogger().warning(String.format("error when trying to send message %s to %s: %s", msg, server, e.getMessage()));
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());
    }
}