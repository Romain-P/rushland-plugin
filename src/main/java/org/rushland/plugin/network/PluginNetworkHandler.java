package org.rushland.plugin.network;

import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

/**
 * Managed by romain on 31/10/2014.
 */
public class PluginNetworkHandler implements PluginMessageListener {
    @SneakyThrows
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        DataInput in = new DataInputStream(new ByteArrayInputStream(bytes));

        if(!in.readUTF().equalsIgnoreCase("rushland")) return;


    }
}
