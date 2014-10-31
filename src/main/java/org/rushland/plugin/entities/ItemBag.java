package org.rushland.plugin.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
    private final int itemId;

    @QueryField
    private final String name;

    @QueryField
    private final String allowedGames;

    private List<GameType> allowedGamesCache;

    @Inject PluginFactory factory;

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

    public List<GameType> getAllowedGames() {
        if (allowedGamesCache == null) {
            allowedGamesCache = parseAllowedGames();
        }
        return allowedGamesCache;
    }

    public boolean isGameAllowed(GameType type) {
        return getAllowedGames().contains(type);
    }

    public Item getItem() {
        return factory.getItems().get(itemId);
    }
}
