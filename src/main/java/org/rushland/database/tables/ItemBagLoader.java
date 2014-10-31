package org.rushland.database.tables;

import com.google.inject.Injector;
import lombok.SneakyThrows;
import org.rushland.api.implementations.database.DefaultDlaoQueryManager;
import org.rushland.api.implementations.database.model.DefaultQueryModel;
import org.rushland.api.interfaces.database.model.Query;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.entities.ItemBag;

import javax.inject.Inject;

/**
 * @author Blackrush
 */
public class ItemBagLoader extends DefaultDlaoQueryManager {
    @Inject PluginFactory factory;
    @Inject Injector injector;

    public ItemBagLoader() {
        super(new DefaultQueryModel<>("item_bags", ItemBag.class));
    }

    @SneakyThrows
    @Override
    public void loadAll() {
        for (Query query : createNewQueries()) {
            ItemBag itemBag = new ItemBag(
                    (int) query.getData().get("id"),
                    (String) query.getData().get("itemsId"),
                    (String) query.getData().get("name"),
                    (String) query.getData().get("allowedGames")
            );
            injector.injectMembers(itemBag);

            factory.getItemBags().put(itemBag.getId(), itemBag);
        }
    }
}
