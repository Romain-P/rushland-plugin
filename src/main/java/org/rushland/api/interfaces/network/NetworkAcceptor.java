package org.rushland.api.interfaces.network;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Return on 02/09/2014.
 */
public abstract class NetworkAcceptor extends NetworkService {
    protected final IoAcceptor acceptor;

    public NetworkAcceptor() {
        super(new NioSocketAcceptor());
        this.acceptor = (IoAcceptor) service;
    }

    @Override
    public boolean start(String ip, int port) throws IOException {
        configure();
        acceptor.bind(new InetSocketAddress(ip, port));
        return acceptor.isActive();
    }

    @Override
    public void stop() {
        for(IoSession session: acceptor.getManagedSessions().values())
            if(!session.isClosing())
                session.close(false);
        acceptor.dispose(false);
    }
}
