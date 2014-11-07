package org.rushland.api.interfaces.games;

import org.rushland.plugin.entities.Client;

/**
 * Managed by romain on 01/11/2014.
 */
public interface GameMod {
    void addClient(Client client);
    void delClient(Client client);
}
