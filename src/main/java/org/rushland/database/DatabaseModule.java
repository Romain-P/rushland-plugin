package org.rushland.database;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.rushland.api.interfaces.database.DaoQueryManager;
import org.rushland.api.interfaces.database.DatabaseService;
import org.rushland.api.interfaces.database.DlaoQueryManager;
import org.rushland.database.tables.ClientManager;
import org.rushland.database.tables.GradeLoader;

/**
 * Managed by romain on 29/10/2014.
 */
public class DatabaseModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(DatabaseService.class).to(RushlandDatabaseService.class);

        Multibinder<DaoQueryManager> managers = Multibinder.newSetBinder(binder(), DaoQueryManager.class);
        managers.addBinding().to(ClientManager.class).asEagerSingleton();

        Multibinder<DlaoQueryManager> loaders = Multibinder.newSetBinder(binder(), DlaoQueryManager.class);
        loaders.addBinding().to(GradeLoader.class).asEagerSingleton();
    }
}
