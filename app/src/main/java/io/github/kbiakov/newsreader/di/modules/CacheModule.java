package io.github.kbiakov.newsreader.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.kbiakov.newsreader.BuildConfig;
import io.github.kbiakov.newsreader.models.entities.Models;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;

@Module
public class CacheModule {

    @Provides
    @Singleton
    ReactiveEntityStore<Persistable> provideDbStore(Context context) {
        // override onUpgrade to handle migrating to a new version
        DatabaseSource source = new DatabaseSource(context, Models.DEFAULT, 1);
        if (BuildConfig.DEBUG) {
            // drop and recreate the tables on every upgrade
            source.setTableCreationMode(TableCreationMode.DROP_CREATE);
        }
        Configuration config = source.getConfiguration();
        return ReactiveSupport.toReactiveStore(new EntityDataStore<>(config));
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
