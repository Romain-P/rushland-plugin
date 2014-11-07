package org.rushland.core;

import com.google.inject.AbstractModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Managed by romain on 29/10/2014.
 */
public class CoreModule extends AbstractModule {
    private final JavaPlugin plugin;

    public CoreModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class).toInstance(plugin);
    }
}
