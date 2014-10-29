package org.rushland.database.tables;

import com.google.inject.Inject;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.implementations.database.DefaultDaoQueryManager;
import org.rushland.api.implementations.database.model.DefaultQueryModel;
import org.rushland.api.interfaces.database.model.Query;
import org.rushland.api.interfaces.database.model.enums.OnlyExecuteQueryEnum;
import org.rushland.plugin.entities.Client;

import java.sql.SQLException;

/**
 * Managed by romain on 29/10/2014.
 */
public class ClientManager extends DefaultDaoQueryManager<Client>{
    @Inject
    JavaPlugin plugin;

    public ClientManager() {
        super(new DefaultQueryModel<>("clients", new Client()).schematize());
    }

    @Override
    public boolean create(Client obj) {
        try {
            Query query = model.createNewQuery()
                    .setData("uuid", obj.getUuid())
                    .setData("name", obj.getName())
                    .setData("money", obj.getMoney())
                    .setData("pvmDeaths", obj.getPvmDeaths())
                    .setData("pvmWins", obj.getPvmWins())
                    .setData("pvpDeaths", obj.getPvpDeaths())
                    .setData("pvpWins", obj.getPvpWins())
                    .setData("pvmRatio", obj.getPvmRatio())
                    .setData("pvpRatio", obj.getPvpRatio());
            execute(query, OnlyExecuteQueryEnum.CREATE);
        } catch (Exception exception) {
            plugin.getLogger().warning(exception.getMessage());
            return false;
        }
        return true;
    }
    @Override
    public boolean delete(Client obj) {
        try {
            execute(model, obj.getName(), OnlyExecuteQueryEnum.DELETE);
        } catch (Exception exception) {
            plugin.getLogger().warning(exception.getMessage());
            return false;
        }
        return true;
    }
    @Override
    public boolean update(Client obj) {
        try {
            Query query = model.createNewQuery()
                    .setData("uuid", obj.getUuid())
                    .setData("name", obj.getName())
                    .setData("money", obj.getMoney())
                    .setData("pvmDeaths", obj.getPvmDeaths())
                    .setData("pvmWins", obj.getPvmWins())
                    .setData("pvpDeaths", obj.getPvpDeaths())
                    .setData("pvpWins", obj.getPvpWins())
                    .setData("pvmRatio", obj.getPvmRatio())
                    .setData("pvpRatio", obj.getPvpRatio());
            execute(query, OnlyExecuteQueryEnum.UPDATE);
        } catch (Exception exception) {
            plugin.getLogger().warning(exception.getMessage());
            return false;
        }
        return true;
    }
    @Override
    public Client load(Object primary) {
        try {
            Query query = createNewQuery(primary);
            return new Client(
                    (String) query.getData().get("uuid"),
                    (String) query.getData().get("name"),
                    (long) query.getData().get("money"),
                    (int) query.getData().get("pvmDeaths"),
                    (int) query.getData().get("pvmWins"),
                    (int) query.getData().get("pvpDeaths"),
                    (int) query.getData().get("pvpWins"));
        } catch (SQLException exception) {
            plugin.getLogger().warning(exception.getMessage());
            return null;
        }
    }
}
