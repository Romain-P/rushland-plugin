package org.rushland.plugin.listeners;

import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.rushland.api.interfaces.bukkit.ImprovedListener;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.enums.BoardState;
import org.rushland.plugin.enums.PluginType;
import org.rushland.plugin.games.entities.GameProfile;

/**
 * Managed by romain on 22/11/2014.
 */
public class ClientGameListener extends ImprovedListener {
    @Inject
    PluginFactory factory;
    @Inject
    JavaPlugin plugin;

    @EventHandler
    public void onPlayerSave(PlayerBedEnterEvent event) {
        if(factory.getType() != PluginType.LOBBY) return;

        Player character = get(event);

        if(character.getBedSpawnLocation() == null) {
            Client client = factory.getClients().get(character.getUniqueId().toString());
            for(Client player: client.getGameProfile().getTeam().getClients()) {
                player.getPlayer().setBedSpawnLocation(event.getBed().getLocation());

                if(!player.equals(client))
                    player.getPlayer().sendMessage("Votre cohéquipier a dormi pour vous!");
                player.getPlayer().sendMessage("Votre position est sauvegardée.");
            }
        } else {
            character.sendMessage("Votre position a déjà été sauvegardée..");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent event) {
        if(factory.getType() != PluginType.LOBBY) return;
        Player character = get(event);

        //check if he has a bedSpawnLoc
        if(character.getBedSpawnLocation() == null) {
            Client client = factory.getClients().get(character.getUniqueId().toString());
            client.setDied(true);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(factory.getType() != PluginType.LOBBY) return;

        Client client = factory.getClients().get(event.getPlayer().getUniqueId().toString());
        GameProfile gameProfile = client.getGameProfile();

        if(gameProfile.getGame().getState() != BoardState.FULL) {
            int[] pos = gameProfile.getTeam().getWaitSpawn();
            event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), pos[0], pos[1], pos[2]));
        }
        else if (client.isDied()) {
            client.getGameProfile().getGame().delClient(client);
            client.setDied(false);
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        if(event.getItemDrop().getItemStack().getType() == Material.BED) {
            Client client = factory.getClients().get(event.getPlayer().getUniqueId().toString());
            int[] cords = client.getGameProfile().getTeam().getSpawn();
            Location bedLoc = new Location(event.getPlayer().getWorld(), cords[0], cords[1], cords[2]);

            event.getPlayer().getWorld().dropItem(bedLoc, event.getItemDrop().getItemStack());
            event.getItemDrop().remove();

            for(Client mate: client.getGameProfile().getTeam().getClients())
                if(!mate.equals(client))
                    mate.getPlayer().sendMessage("Le lit a respawn à la position de départ, pensez à aller sauvegarder votre position.");
        }
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageByEntityEvent event) {
        if(factory.getType() != PluginType.LOBBY) return;

        //check if they're players
        Entity attackerEntity = event.getDamager();
        Entity damagedEntity = event.getEntity();

        if(!(attackerEntity instanceof Player) || !(damagedEntity instanceof Player)) return;

        Client attacker = factory.getClients().get(attackerEntity.getUniqueId().toString());
        Client damaged = factory.getClients().get(damagedEntity.getUniqueId().toString());

        if(attacker.getGameProfile().getTeam().equals(damaged.getGameProfile().getTeam()))
            event.setCancelled(true);
    }
}
