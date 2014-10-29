package org.rushland.api.interfaces.bukkit;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;

/**
 * Managed by romain on 08/10/2014.
 */
public abstract class ImprovedListener extends Pullable implements Listener{
    protected Player get(PlayerEvent event) {
        return event.getPlayer();
    }

    protected Player get(EntityEvent event) {
        return getPlayer(event.getEntity());
    }

    protected boolean isPlayer(Entity entity) {
        return entity instanceof Player;
    }
    protected Player getPlayer(Entity entity) {
        return entity instanceof Player ? (Player) entity : null;
    }

    public void pullPlayer(Player player) {}
}
