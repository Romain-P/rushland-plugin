package org.rushland.plugin.entities;

import com.google.inject.Inject;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.interfaces.database.DaoQueryManager;
import org.rushland.api.interfaces.database.model.annotations.PrimaryQueryField;
import org.rushland.api.interfaces.database.model.annotations.QueryField;
import org.rushland.database.RushlandDatabaseService;
import org.rushland.plugin.PluginFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    @QueryField
    private int grade;
    @QueryField
    private long gradeTime;
    @Getter
    private Player player;
    private final DaoQueryManager<Client> manager;

    @Inject
    RushlandDatabaseService database;
    @Inject
    JavaPlugin plugin;
    @Inject
    PluginFactory factory;

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
        return player != null ? player : (player = plugin.getServer().getPlayer(UUID.fromString(uuid)));
    }

    public void subscribe(int grade, long gradeTime, TimeUnit unity) {
        this.grade = grade;
        this.gradeTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(gradeTime, unity);

        save();
    }

    public void unsubscribe() {
        if(player != null)
            player.setDisplayName(player.getDisplayName().replace(factory.getGrades().get(grade).getPrefix(), ""));

        grade = 0;
        gradeTime = 0;
        save();
    }

    private boolean isSubscriber() {
        boolean subscribed = this.gradeTime == -1 ||
                System.currentTimeMillis() < this.gradeTime;
        if(!subscribed && this.gradeTime > 0)
            this.unsubscribe();
        return subscribed;
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