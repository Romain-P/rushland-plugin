package org.rushland.plugin;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.rushland.api.interfaces.bukkit.ImprovedListener;
import org.rushland.plugin.entities.Client;
import org.rushland.plugin.games.GameManager;
import org.rushland.plugin.listeners.*;
import org.rushland.plugin.network.PluginNetworkService;

/**
 * Managed by romain on 30/10/2014.
 */
public class PluginModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(PluginFactory.class).asEagerSingleton();
        bind(Client.class).asEagerSingleton();
        bind(PluginNetworkService.class).asEagerSingleton();
        bind(GameManager.class).asEagerSingleton();

        Multibinder<ImprovedListener> listeners = Multibinder.newSetBinder(binder(), ImprovedListener.class);
        listeners.addBinding().to(ClientLogListener.class).asEagerSingleton();
        listeners.addBinding().to(BoardGameListener.class).asEagerSingleton();
        listeners.addBinding().to(CustomItemListener.class).asEagerSingleton();
        listeners.addBinding().to(ClientGameListener.class).asEagerSingleton();
        listeners.addBinding().to(ClientDefaultListener.class).asEagerSingleton();
    }
}
