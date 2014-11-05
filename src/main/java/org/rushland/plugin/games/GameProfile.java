package org.rushland.plugin.games;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.rushland.api.interfaces.games.GameMod;
import org.rushland.plugin.entities.Client;

import java.util.List;

/**
 * Managed by romain on 01/11/2014.
 */
@RequiredArgsConstructor
@Getter
public class GameProfile {
    private final GameMod game;
    private final List<Client> team;
}
