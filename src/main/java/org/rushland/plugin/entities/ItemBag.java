package org.rushland.plugin.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.rushland.api.interfaces.database.model.annotations.PrimaryQueryField;
import org.rushland.api.interfaces.database.model.annotations.QueryField;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.enums.GameType;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ItemBag {
    @PrimaryQueryField
    private final int id;

    @QueryField
    private final String itemsId;

    @QueryField
    private final String name;

    @QueryField
    private final String allowedGames;

    private List<Integer> itemsIdCache;
    private List<GameType> allowedGamesCache;

    @Inject PluginFactory factory;

    List<Integer> parseItemsId() {
        List<Integer> result = new ArrayList<>();

        for (String itemId : itemsId.split(",")) {
            int id = Integer.parseInt(itemId);
            result.add(id);
        }

        return result;
    }

    List<GameType> parseAllowedGames() {
        List<GameType> result = new ArrayList<>();

        for (String game : allowedGames.split(",")) {
            try {
                GameType type = GameType.valueOf(game.toUpperCase());
                result.add(type);
            } catch (IllegalArgumentException ex) {
                // TODO log some kind of warning
            }
        }

        return result;
    }

    public List<Integer> getItemsId() {
        if (itemsIdCache == null) {
            itemsIdCache = parseItemsId();
        }
        return itemsIdCache;
    }

    public List<GameType> getAllowedGames() {
        if (allowedGamesCache == null) {
            allowedGamesCache = parseAllowedGames();
        }
        return allowedGamesCache;
    }

    public boolean isGameAllowed(GameType type) {
        return getAllowedGames().contains(type);
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        for (Integer itemId : getItemsId()) {
            Item item = factory.getItems().get(itemId);
            items.add(item);
        }
        return items;
    }

    public Inventory generateInventory() {
        List<Item> items = getItems();
        Inventory result = Bukkit.createInventory(null, (items.size() % 9 + 1) * 9, name);
        for (Item item : items) {
            result.addItem(item.generate());
        }
        return result;
    }
}
