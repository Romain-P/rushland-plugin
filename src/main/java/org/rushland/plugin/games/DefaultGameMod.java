package org.rushland.plugin.games;

import lombok.Getter;
import org.rushland.api.interfaces.games.GameMod;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.enums.GameType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Managed by romain on 01/11/2014.
 */
public class DefaultGameMod implements GameMod {
    @Getter
    private final List<List<Client>> teams;
    @Getter
    private final String name;
    private final GameType type;
    private final int nbrTeam, nbrPerTeam;

    public DefaultGameMod(String name, GameType type, int nbrTeam, int nbrPerTeam) {
        this.teams = Collections.synchronizedList(new ArrayList<List<Client>>(nbrTeam));
        for(int i=0; i < nbrTeam; i++)
            this.teams.add(Collections.synchronizedList(new ArrayList<Client>(nbrPerTeam)));

        this.name = name;
        this.type = type;
        this.nbrTeam = nbrTeam;
        this.nbrPerTeam = nbrPerTeam;
    }

    public boolean isAvailable() {
        for(List<Client> team: teams)
            if(team.size() < nbrPerTeam)
                return true;
        return false;
    }

    public void addClient(Client client) {
        List<Client> clientTeam = null;

        for(List<Client> team: teams)
            if(team.size() < nbrPerTeam)
                (clientTeam = team).add(client);

        client.setGameProfile(new GameProfile(this, clientTeam));
        check();
    }

    public void delClient(Client client) {
        client.getGameProfile().getTeam().remove(client);
        client.setGameProfile(null);
    }

    private void check() {
        boolean full = true;

        for(List<Client> team: teams)
            if(team.size() < nbrPerTeam)
                full = false;

        if(full) start();
    }

    private void start() {

    }
}
