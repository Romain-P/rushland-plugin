package org.rushland.api.interfaces.games;

import org.rushland.plugin.entities.Client;
import org.rushland.plugin.enums.BoardState;

/**
 * Managed by romain on 01/11/2014.
 */
public interface GameMod {
    void addClient(Client client);
    void delClient(Client client);
    BoardState getState();
}
