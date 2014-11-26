package org.rushland.plugin.network;

import com.google.inject.Inject;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Managed by romain on 31/10/2014.
 */
public class PluginNetworkHandler implements IoHandler {
    @Inject
    JavaPlugin plugin;
    @Inject
    PluginNetworkService network;

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        network.setNetwork(session);
        plugin.getLogger().info("(Network) adding new server <=> "+session.getId());
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        plugin.getLogger().info("(Network) opened server's session "+session.getId());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        plugin.getLogger().info("(Network) closed server's session " + session.getId());
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        plugin.getLogger().warning("(Network) Error to session " + session.getId() + " : " + cause.getMessage());
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String packet = (String) message;

        String[] toParse = packet.split("\\|");

        for(int i=toParse.length ; i > 0 ; i--) {
            String msg = toParse[toParse.length - i];
            network.parse(msg);
            plugin.getLogger().info("(network) <- "+msg);
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        plugin.getLogger().info("(network) -> " + message.toString());
    }
}
