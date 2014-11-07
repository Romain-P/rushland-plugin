package org.rushland.plugin.games;

import lombok.Getter;
import org.bukkit.block.Sign;
import org.rushland.plugin.enums.BoardLines;

import java.util.ArrayList;
import java.util.List;

/**
 * Managed by romain on 01/11/2014.
 */
@Getter
public class GameSign {
    private final Sign sign;
    private final int maxClients;
    private final List<String> clients;
    private final String lobby;

    public GameSign(Sign sign, String lobby) {
        this.sign = sign;
        this.maxClients = Integer.parseInt(sign.getLine(BoardLines.PLAYERS.get()).split("/")[1]);
        this.clients = new ArrayList<>();
        this.lobby = lobby;
    }
}
