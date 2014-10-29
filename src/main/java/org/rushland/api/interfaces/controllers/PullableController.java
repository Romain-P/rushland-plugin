package org.rushland.api.interfaces.controllers;

import org.bukkit.entity.Player;
import org.rushland.api.interfaces.bukkit.Pullable;

/**
 * Managed by romain on 08/10/2014.
 */
public abstract class PullableController extends Pullable implements Controller{
    public void pullPlayer(Player player) {}
}
