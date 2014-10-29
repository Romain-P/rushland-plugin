package org.rushland.api.interfaces.controllers;

import com.google.common.collect.ImmutableList;
import org.rushland.api.interfaces.bukkit.ImprovedListener;

/**
 * Managed by romain on 07/10/2014.
 */
public interface Controller {
    ImmutableList<ImprovedListener> getListeners();
    void start();
    void stop(boolean pluginStopped);
}
