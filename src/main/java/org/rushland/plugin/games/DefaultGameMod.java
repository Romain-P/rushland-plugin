package org.rushland.plugin.games;

import lombok.Getter;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.interfaces.games.GameMod;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.enums.GameType;
import org.rushland.plugin.games.entities.GameProfile;
import org.rushland.plugin.games.entities.GameTeam;
import org.rushland.plugin.games.entities.GameTypeProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Managed by romain on 01/11/2014.
 */
public class DefaultGameMod implements GameMod {
    @Getter
    private final List<GameTeam> teams;
    @Getter
    private final String name;
    private final GameType type;
    private final int nbrTeam, nbrPerTeam;
    private final String mapPath, waitMapPath;
    private final int[][] mapSpawns, waitMapSpawns;
    private final World world, waitWorld;

    private final static String INST_MAP_PATH = "plugins/rushland/maps/instances";

    public DefaultGameMod(String name, GameTypeProperty property, int nbrTeam, int nbrPerTeam, JavaPlugin plugin) {
        this.teams = Collections.synchronizedList(new ArrayList<GameTeam>(nbrTeam));
        this.name = name;
        this.type = property.getType();
        this.nbrTeam = nbrTeam;
        this.nbrPerTeam = nbrPerTeam;
        this.mapPath = property.getMapPath();
        this.waitMapPath = property.getWaitMapPath();
        this.mapSpawns = property.getMapSpawns().toArray(new int[][] {});
        this.waitMapSpawns = property.getWaitMapSpawns().toArray(new int[][] {});

        for(int i=0; i < nbrTeam; i++)
            this.teams.add(new GameTeam(Collections.synchronizedList(new ArrayList<Client>(nbrPerTeam)), mapSpawns[i], waitMapSpawns[i]));

        this.world = plugin.getServer().createWorld(new WorldCreator(mapPath));
        this.waitWorld = plugin.getServer().createWorld(new WorldCreator(waitMapPath));
    }

    public boolean isAvailable() {
        for(GameTeam team: teams)
            if(team.getClients().size() < nbrPerTeam)
                return true;
        return false;
    }

    public void addClient(Client client) {
        GameTeam gameTeam = null;

        for(GameTeam team: teams) {
            if (team.getClients().size() < nbrPerTeam) {
                (gameTeam = team).getClients().add(client);
                break;
            }
        }

        client.setGameProfile(new GameProfile(this, gameTeam));
        check();
    }

    public void delClient(Client client) {
        client.getGameProfile().getTeam().getClients().remove(client);
        client.setGameProfile(null);
    }

    private void check() {
        boolean full = true;

        for(GameTeam team: teams)
            if(team.getClients().size() < nbrPerTeam)
                full = false;

        if(full) start();
    }

    private void start() {

    }
}
