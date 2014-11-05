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
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.enums.PluginType;
import org.rushland.plugin.games.GameManager;

/**
 * Managed by romain on 31/10/2014.
 */
public class BoardGameListener extends ImprovedListener{
    @Inject
    PluginFactory factory;
    @Inject
    GameManager gameManager;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(factory.getType() != PluginType.MAIN || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if(block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) return;

        Sign sign = (Sign) block.getState(); //block instanceof Sign ? (Sign) block.getState() : null;

        Client client = factory.getClients().get(event.getPlayer().getUniqueId().toString());
        gameManager.askJoin(client, sign);
    }
}
