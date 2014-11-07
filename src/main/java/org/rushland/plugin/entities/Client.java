package org.rushland.plugin.entities;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.interfaces.database.model.annotations.PrimaryQueryField;
import org.rushland.api.interfaces.database.model.annotations.QueryField;
import org.rushland.database.PluginDatabaseService;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.games.GameProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Managed by romain on 29/10/2014.
 */
public class Client {
    @Getter
    @PrimaryQueryField
    private final String uuid;
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
    @QueryField
    private int grade;
    @Getter
    @QueryField
    private long gradeTime;
    private Player player;
    @Getter
    private final List<String> cachedLobbies;
    @Getter
    @Setter
    private GameProfile gameProfile;

    @Inject
    PluginDatabaseService database;
    @Inject
    JavaPlugin plugin;
    @Inject
    PluginFactory factory;

    public Client() {
        this(null, "auto");
    }

    public Client(String uuid, String name) {
        this(uuid, 0, 0, 0, 0, 0, 0, 0);
    }

    public Client(String uuid) {
        this(uuid, 0, 0, 0, 0, 0, 0, 0);
    }

    public Client(String uuid, long money, int pvmDeaths, int pvmWins, int pvpDeaths, int pvpWins, int grade, long gradeTime) {
        this.uuid = uuid;
        this.money = money;
        this.pvmDeaths = pvmDeaths;
        this.pvmWins = pvmWins;
        this.pvpDeaths = pvpDeaths;
        this.pvpWins = pvpWins;
        this.grade = grade;
        this.gradeTime = gradeTime;
        this.cachedLobbies = new ArrayList<>();
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
        return (pvmRatio = Math.floor((pvmWins/(pvmDeaths==0?1:pvmDeaths)) * 100)) / 100;
    }

    public double getPvpRatio() {
        return (pvpRatio = Math.floor((pvpWins/(pvpDeaths==0?1:pvpDeaths)) * 100)) / 100;
    }

    public Player getPlayer() {
        return player != null ? player : (player = plugin.getServer().getPlayer(UUID.fromString(uuid)));
    }

    public String getName() {
        return getPlayer().getName();
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

    public void addNewCachedLobby(String name) {
        if(!cachedLobbies.contains(name))
            cachedLobbies.add(name);
    }

    public void save() {
        factory.getClientManager().update(this);
    }

    public Client create() {
        factory.getClientManager().create(this);
        return this;
    }

    public void remove() {
        factory.getClientManager().delete(this);
    }
}