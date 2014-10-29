package org.rushland.database;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.rushland.api.interfaces.database.DaoQueryManager;
import org.rushland.api.interfaces.database.DatabaseService;
import org.rushland.database.tables.ClientManager;

/**
 * Managed by romain on 29/10/2014.
 */
public class DatabaseModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(DatabaseService.class).to(RushlandDatabaseService.class);

        Multibinder<DaoQueryManager> tables = Multibinder.newSetBinder(binder(), DaoQueryManager.class);
        tables.addBinding().to(ClientManager.class).asEagerSingleton();
    }
}
