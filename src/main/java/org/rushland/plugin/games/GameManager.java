package org.rushland.plugin.games;

import com.google.inject.Inject;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.rushland.api.interfaces.games.GameMod;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.enums.BoardLines;
import org.rushland.plugin.enums.GameType;
import org.rushland.plugin.network.PluginNetworkService;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Managed by romain on 01/11/2014.
 */
public class GameManager {
    /** Type Main **/
    private final Map<String, GameSign> games, clients;
    private final Map<String, Integer> lobbies;
    /** Type Lobby **/
    private final Map<String, GameMod> gamemods;

    private final ReentrantLock locker;

    @Inject
    PluginNetworkService network;
    PluginFactory factory;

    @Inject
    public GameManager(PluginFactory factory) {
        this.games = new HashMap<>();
        this.clients = new HashMap<>();
        this.lobbies = new HashMap<>();
        this.gamemods = new HashMap<>();
        this.locker = new ReentrantLock();

        for(String lobby: factory.getLobbyNames())
            lobbies.put(lobby, 0);
        this.factory = factory;
    }

    public void askJoin(Client client, Sign sign) {
        locker.lock();
        try {
            GameSign game = games.get(sign.getLine(BoardLines.NAME.get()));
            if (game == null)
                games.put(sign.getLine(BoardLines.NAME.get()), (game = new GameSign(sign, availableLobby())));

            if (sign.getLine(BoardLines.STATE.get()).equals("Complet")) {
                client.getPlayer().sendMessage(String.format("%sAction impossible:%s la partie est complète!", ChatColor.RED, ChatColor.RESET));
                return;
            }

            game.getClients().add(client.getUuid());
            lobbies.put(game.getLobby(), lobbies.get(game.getLobby())+1);
            clients.put(client.getUuid(), game);

            updateBoardPlayers(game);
            updateBoardState(game);

            network.dispatchTo(client.getPlayer(), game.getLobby());
            network.sendMessage(client.getPlayer(), game.getLobby(),
                    String.format("game:%s:%s:%s", sign.getLine(BoardLines.NAME.get()), sign.getLine(BoardLines.GAME_TYPE.get()), game.getMaxClients()));
        } finally {
            locker.unlock();
        }
    }

    public void exit(Client client) {
        locker.lock();
        try {
            GameSign game = clients.remove(client.getUuid());
            game.getClients().remove(client.getUuid());

            if (game.getSign().getLine(BoardLines.STATE.get()).equals("Disponible") || game.getClients().size() == 0) {
                if (game.getSign().getLine(BoardLines.STATE.get()).equals("Complet"))
                    lobbies.put(game.getLobby(), lobbies.get(game.getLobby()) - 1);
                updateBoardPlayers(game);
                updateBoardState(game);
            }
        } finally {
            locker.unlock();
        }
    }

    private String availableLobby() {
        for(Map.Entry<String, Integer> entry: lobbies.entrySet())
            if(entry.getValue() < factory.getSlots())
                return entry.getKey();

        //ne devrait pas arriver
        return lobbies.keySet().toArray(new String[1])[new Random().nextInt(lobbies.size())];
    }

    private void updateBoardPlayers(GameSign game) {
        Sign sign = game.getSign();
        sign.setLine(BoardLines.PLAYERS.get(), String.format("%d/%d", game.getClients().size(), game.getMaxClients()));
        sign.update();
    }

    private void updateBoardState(GameSign game) {
        String text = game.getClients().size() == game.getMaxClients() ? "Complet" : "Disponible";
        Sign sign = game.getSign();
        sign.setLine(BoardLines.STATE.get(), text);
        sign.update();
    }

    public void joinGame(Client client, String name, GameType type, int maxPlayers) {
        GameMod gamemod = gamemods.get(name);
        if(gamemod == null)
            gamemods.put(name, (gamemod = new DefaultGameMod(name, type, type == GameType.ANTWAR ? 4 : 2, maxPlayers/2)));
        gamemod.addClient(client);
    }
}
