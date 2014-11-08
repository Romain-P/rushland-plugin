package org.rushland.plugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.rushland.api.interfaces.bukkit.ImprovedListener;
import org.rushland.plugin.PluginFactory;
import org.rushland.plugin.entities.ItemBag;

import javax.inject.Inject;

public class CustomItemListener extends ImprovedListener {

    @Inject PluginFactory factory;

    @EventHandler
    public void onConnect(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();

        for (ItemBag bag : factory.getItemBags().values()) {
            // TODO verify player's game-mode

            ItemStack stack = bag.getIcon().generate();
            player.getInventory().addItem(stack);
        }
    }
}
