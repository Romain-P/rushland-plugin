package org.rushland.plugin.entities;

import com.google.inject.Inject;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.interfaces.database.DaoQueryManager;
import org.rushland.api.interfaces.database.model.annotations.PrimaryQueryField;
import org.rushland.api.interfaces.database.model.annotations.QueryField;
import org.rushland.database.RushlandDatabaseService;

import java.util.UUID;

/**
 * Managed by romain on 29/10/2014.
 */
public class Client {
    @Getter
    @PrimaryQueryField
    private final String uuid;
    @Getter
    @QueryField
    private String name;
    @Getter
    @QueryField
    private long money;
    @Getter
    @QueryField
    private int pvmDeaths, pvmWins, pvpDeaths, pvpWins;
    @QueryField
    private double pvmRatio, pvpRatio;
    @Getter
    private Player player;
    private final DaoQueryManager<Client> manager;

    @Inject
    RushlandDatabaseService database;
    @Inject
    JavaPlugin plugin;

    public Client() {this(null);}

    public Client(String uuid) {
        this(uuid, null, 0, 0, 0, 0, 0);
    }

    public Client(String uuid, String name, long money, int pvmDeaths, int pvmWins, int pvpDeaths, int pvpWins) {
        this.uuid = uuid;
        this.name = name == null ? plugin.getServer().getPlayer(UUID.fromString(uuid)).getName() : name;
        this.money = money;
        this.pvmDeaths = pvmDeaths;
        this.pvmWins = pvmWins;
        this.pvpDeaths = pvpDeaths;
        this.pvpWins = pvpWins;
        this.manager = (DaoQueryManager<Client>) database.getQueryManagers().get(getClass());
    }

    public void addMoney(long money) {
        this.money += money;
    }

    public void delMoney(long money) {
        this.money -= money;
    }

    public void addPvmDeath() {
        this.pvmDeaths++;
    }

    public void addPvmWin() {
        this.pvmWins++;
    }

    public void addPvpDeath() {
        this.pvpDeaths++;
    }

    public void addPvpWin() {
        this.pvpWins++;
    }

    public double getPvmRatio() {
        return (pvmRatio = Math.floor((pvmWins/pvmDeaths) * 100)) / 100;
    }

    public double getPvpRatio() {
        return (pvpRatio = Math.floor((pvpWins/pvpDeaths) * 100)) / 100;
    }

    public Player getPlayer() {
        return player != null ? player : (player = plugin.getServer().getPlayer(name));
    }

    public void save() {
        manager.update(this);
    }

    public Client create() {
        manager.create(this);
        return this;
    }

    public void remove() {
        manager.delete(this);
    }
}