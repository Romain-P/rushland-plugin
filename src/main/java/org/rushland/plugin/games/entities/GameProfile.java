package org.rushland.plugin.games.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.rushland.api.interfaces.games.GameMod;

/**
 * Managed by romain on 01/11/2014.
 */
@RequiredArgsConstructor
@Getter
public class GameProfile {
    private final GameMod game;
    private final GameTeam team;
}
