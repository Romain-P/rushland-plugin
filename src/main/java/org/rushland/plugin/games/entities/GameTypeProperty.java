package org.rushland.plugin.games.entities;

import lombok.Getter;
import org.rushland.plugin.enums.GameType;

import java.util.ArrayList;
import java.util.List;

/**
 * Managed by romain on 06/11/2014.
 */
@Getter
public class GameTypeProperty {
    private final String name;
    private final String mapPath;
    private final String waitMapPath;
    private final List<int[]> mapSpawns;
    private final List<int[]> waitMapSpawns;

    public GameTypeProperty(String name, String mapPath, String waitMapPath, String[] ms, String[] ws) {
        this.name = name;
        this.mapPath = mapPath;
        this.waitMapPath = waitMapPath;
        this.mapSpawns = new ArrayList<>();
        this.waitMapSpawns = new ArrayList<>();

        for(String data: ms) {
            String[] s = data.split(" ");
            mapSpawns.add(new int[]{Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2])});
        }

        for(String data: ws) {
            String[] s = data.split(" ");
            waitMapSpawns.add(new int[]{Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2])});
        }
    }

    public GameType getType() {
        return GameType.valueOf(name.substring(1, name.length() - 1).replace("-", "_").toUpperCase());
    }
}
