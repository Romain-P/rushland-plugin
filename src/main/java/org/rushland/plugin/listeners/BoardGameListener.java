package org.rushland.plugin.listeners;

import com.google.inject.Inject;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.rushland.api.interfaces.bukkit.ImprovedListener;
import org.rushland.plugin.PluginFactory;

/**
 * Managed by romain on 31/10/2014.
 */
public class BoardGameListener extends ImprovedListener{
    @Inject
    PluginFactory factory;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if(block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) return;

        Sign sign = block instanceof Sign ? (Sign) block.getState() : null;
        if(sign == null) return;

        //TODO: create or join a game
    }
}
