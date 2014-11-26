package org.rushland.plugin.games;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.interfaces.games.GameMod;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.enums.BoardState;
import org.rushland.plugin.enums.GameType;
import org.rushland.plugin.games.entities.GameProfile;
import org.rushland.plugin.games.entities.GameTeam;
import org.rushland.plugin.games.entities.GameTypeProperty;
import org.rushland.utils.FileUtils;
import org.rushland.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Managed by romain on 01/11/2014.
 */
public class DefaultGameMod implements GameMod {
    private final ReadWriteLock locker;
    private final JavaPlugin plugin;
    private final GameManager manager;
    @Getter
    private final List<GameTeam> teams;
    @Getter
    private final String name;
    private final GameType type;
    private final int nbrTeam, nbrPerTeam;
    private final String mapPath, waitMapPath;
    private final int[][] mapSpawns, waitMapSpawns;
    private final World waitWorld;
    private final String waitMapInstance;

    //mutable, changées à chaque nouvelle partie
    private World world;
    private String mapInstance;

    //time
    private long lastActivity;
    private int taskId;
    private BoardState state;
    private int timingTask, timingCount;

    public DefaultGameMod(String name, GameTypeProperty property, int nbrTeam, int nbrPerTeam, JavaPlugin plugin, GameManager manager) {
        this.locker = new ReentrantReadWriteLock();
        this.plugin = plugin;
        this.manager = manager;
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

        this.waitWorld = plugin.getServer().createWorld(new WorldCreator((waitMapInstance = FileUtils.copy(waitMapPath))));
    }

    public DefaultGameMod activeTimeOut() {
        taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if(System.currentTimeMillis() - lastActivity < 30*10*1000) return;
                for(GameTeam team: teams) if(!team.getClients().isEmpty()) return;

                plugin.getServer().unloadWorld(waitWorld, true);
                FileUtils.remove(waitMapInstance);

                manager.getGamemods().remove(name);
                plugin.getServer().getScheduler().cancelTask((taskId));
            }
        }, TimeUtils.convertSecondsToTicks(10*60), TimeUtils.convertSecondsToTicks(10*60));
        return this;
    }

    private void tick() {
        lastActivity = System.currentTimeMillis();
    }

    private void createWorld() {
        world = plugin.getServer().createWorld(new WorldCreator((mapInstance = FileUtils.copy(mapPath))));
        world.setTime(14000);
    }

    private void removeWorld() {
        if(plugin.getServer().unloadWorld(world, true)) {
            FileUtils.remove(mapInstance);
            mapInstance = null;
        } else {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    removeWorld();
                }
            }, TimeUtils.convertSecondsToTicks(1));
        }
    }

    public void addClient(Client client) {
        tick();
        GameTeam gameTeam = null;

        for(GameTeam team: teams) {
            if (team.getClients().size() < nbrPerTeam) {
                (gameTeam = team).getClients().add(client);
                break;
            }
        }

        assert gameTeam != null;

        client.setGameProfile(new GameProfile(this, gameTeam));
        client.getPlayer().teleport(new Location(waitWorld, gameTeam.getWaitSpawn()[0],gameTeam.getWaitSpawn()[1],gameTeam.getWaitSpawn()[2]));
        check();
    }

    public void delClient(Client client) {
        locker.writeLock().lock();
        try {
            if (state == BoardState.STARTING) {
                plugin.getServer().getScheduler().cancelTask((timingTask));
                state = BoardState.AVAILABLE;
            }

            tick();
            GameTeam team = client.getGameProfile().getTeam();
            team.getClients().remove(client);
            client.setGameProfile(null);

            GameTeam winners = null;
            client.getPlayer().teleport(waitWorld.getSpawnLocation());

            if (state != BoardState.FULL) return;

            for (GameTeam may : teams) {
                if (!may.getClients().isEmpty()) {
                    if (winners == null)
                        winners = may;
                    else {
                        winners = null;
                        break;
                    }
                }
            }

            if (winners != null) {
                giveRewards(winners);
                onEnd();
            }
        } finally {
            locker.writeLock().unlock();
        }
    }

    private boolean check() {
        boolean full = true;

        for(GameTeam team: teams)
            if(team.getClients().size() < nbrPerTeam)
                full = false;

        if(full) {
            locker.writeLock().lock();
            try {
                state = BoardState.STARTING;
            } finally  {
                locker.writeLock().unlock();
            }

            timingCount = 10;
            timingTask = schedule(1, new Runnable() {
                public void run() {
                    locker.readLock().lock();
                    try {
                        if(state == BoardState.AVAILABLE) {
                            sendMapMessage("Partie annulée: en attente de joueurs..");
                            timingTask = 0;
                            return;
                        }
                    } finally {
                        locker.readLock().unlock();
                    }

                    if(timingCount == 1) {
                        start();
                        return;
                    } else {
                        timingCount--;
                        schedule(1, this);
                    }

                    sendMapMessage("Début dans "+timingCount+" secondes..");
                }
            });
        }

        return full;
    }

    private void start() {
        createWorld();

        Location gameLocation;
        for(GameTeam team: teams) {
            gameLocation = new Location(world, team.getSpawn()[0], team.getSpawn()[1], team.getSpawn()[2]);

            for (Client client : team.getClients()) {
                client.reset();
                client.getPlayer().teleport(gameLocation);
                client.getPlayer().setBedSpawnLocation(gameLocation);
                client.getPlayer().sendMessage(Color.RED+"Le jeu commence, bonne chance à tous!");
            }
        }

        timingTask = 0;
        locker.writeLock().lock();
        try {
            state = BoardState.FULL;
        } finally {
            locker.writeLock().unlock();
        }
    }

    @Override
    public void onEnd() {
        state = BoardState.AVAILABLE;
        removeWorld();
    }

    private void giveRewards(GameTeam team) {
        List<Client> mirror = team.getClients(); //concurrent exception fixed
        for(Client client: mirror) {
            team.getClients().remove(client);
            client.setGameProfile(null);
            client.getPlayer().sendMessage(Color.RED+"Félicitations, vous avez gagné!");
            client.returnToMain();
        }
    }

    private int schedule(int seconds, Runnable runnable) {
        return plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, TimeUtils.convertSecondsToTicks(seconds));
    }

    public BoardState getState() {
        locker.readLock().lock();
        try {
            return this.state;
        } finally {
            locker.readLock().unlock();
        }
    }

    private void sendMapMessage(String message) {
        for(GameTeam team: teams)
            for(Client client: team.getClients())
                client.getPlayer().sendMessage(message);
    }
}
