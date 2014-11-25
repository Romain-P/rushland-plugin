package org.rushland.api.interfaces.network;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Return on 02/09/2014.
 */
public abstract class NetworkConnector extends NetworkService {
    protected final IoConnector connector;

    public NetworkConnector() {
        super(new NioSocketConnector());
        this.connector = (IoConnector) service;
    }

    @Override
    public boolean start(String ip, int port) throws IOException {
        configure();
        return connector.connect(new InetSocketAddress(ip, port)).isConnected();
    }

    @Override
    public void stop() {
        for(IoSession session: connector.getManagedSessions().values())
            if(!session.isClosing())
                session.close(false);
        connector.dispose(false);
    }
}
