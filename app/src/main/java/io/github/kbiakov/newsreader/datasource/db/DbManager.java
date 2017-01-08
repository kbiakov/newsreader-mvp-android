package io.github.kbiakov.newsreader.datasource.db;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.github.kbiakov.newsreader.BuildConfig;
import io.github.kbiakov.newsreader.models.Models;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;

public class DbManager {

    private static volatile DbManager instance;

    private ReactiveEntityStore<Persistable> dataStore;

    private DbManager() {}

    public static DbManager getInstance() {
        DbManager localInstance = instance;

        if (localInstance == null) {
            synchronized (DbManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DbManager();
                }
            }
        }
        return localInstance;
    }

    @NonNull
    public ReactiveEntityStore<Persistable> getData() throws RuntimeException {
        if (dataStore == null) {
            // override onUpgrade to handle migrating to a new version
            DatabaseSource source = new DatabaseSource(getReflectedContext(), Models.DEFAULT, 1);
            if (BuildConfig.DEBUG) {
                // use this in development mode to drop and recreate the tables on every upgrade
                source.setTableCreationMode(TableCreationMode.DROP_CREATE);
            }
            Configuration configuration = source.getConfiguration();
            dataStore = ReactiveSupport.toReactiveStore(
                    new EntityDataStore<Persistable>(configuration));
        }
        return dataStore;
    }

    @NonNull
    private Context getReflectedContext() throws RuntimeException {
        try {
            final Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            final Method method = activityThreadClass.getMethod("currentApplication");
            return (Application) method.invoke(null, (Object[]) null);
        } catch (final ClassNotFoundException |
                InvocationTargetException |
                IllegalAccessException |
                IllegalArgumentException |
                NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
