package org.rushland.database.tables;

import lombok.SneakyThrows;
import org.rushland.api.implementations.database.DefaultDlaoQueryManager;
import org.rushland.api.implementations.database.model.DefaultQueryModel;
import org.rushland.api.interfaces.database.model.Query;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.entities.Item;

import javax.inject.Inject;

public class ItemLoader extends DefaultDlaoQueryManager {
    @Inject
    PluginFactory factory;

    public ItemLoader() {
        super(new DefaultQueryModel<>("items", Item.class).schematize());
    }

    @SneakyThrows
    @Override
    public void loadAll() {
        for (Query query : createNewQueries()) {
            Item item = new Item(
                    (int) query.getData().get("id"),
                    (String) query.getData().get("name"),
                    (String) query.getData().get("description"),
                    (int) query.getData().get("grade"),
                    (int) query.getData().get("quantity"),
                    (String) query.getData().get("enchantments")
            );

            factory.getItems().put(item.getId(), item);
        }
    }
}
