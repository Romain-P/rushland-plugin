package org.rushland.database.tables;

import com.google.inject.Inject;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.implementations.database.DefaultDlaoQueryManager;
import org.rushland.api.implementations.database.model.DefaultQueryModel;
import org.rushland.api.interfaces.database.model.Query;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.entities.Grade;
import org.rushland.utils.TextUtils;

import java.sql.SQLException;

/**
 * Managed by romain on 30/10/2014.
 */
public class GradeLoader extends DefaultDlaoQueryManager {
    @Inject
    JavaPlugin plugin;
    @Inject
    PluginFactory factory;

    public GradeLoader()  {
        super(new DefaultQueryModel<>("grades", new Grade()).schematize());
    }

    @Override
    public void loadAll() {
        try {
            Query[] queries = createNewQueries();

            for(Query query: queries) {
                int id = (int) query.getData().get("id");
                String prefix = TextUtils.parseColors((String) query.getData().get("prefix"));

                factory.getGrades().put(id, new Grade(id, prefix));
            }
        } catch (SQLException exception) {
            plugin.getLogger().warning(exception.getMessage());
        }
    }
}
