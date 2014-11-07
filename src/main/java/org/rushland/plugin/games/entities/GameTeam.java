package org.rushland.plugin.games.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.rushland.plugin.entities.Client;

import java.util.List;

/**
 * Managed by romain on 06/11/2014.
 */
@Getter
@RequiredArgsConstructor
public class GameTeam {
    private final List<Client> clients;
    private final int[] spawn;
    private final int[] waitSpawn;
}
