package io.github.kbiakov.newsreader.di.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.kbiakov.newsreader.BuildConfig;
import io.github.kbiakov.newsreader.db.DbStore;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.entities.ArticleEntity;
import io.github.kbiakov.newsreader.models.entities.Models;
import io.github.kbiakov.newsreader.models.entities.Source;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
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
    DbStore provideDbStore(ReactiveEntityStore<Persistable> entityStore) {
        return new DbStore() {
            @Override
            public Observable<List<Source>> getSources() {
                return entityStore
                        .select(Source.class)
                        .get()
                        .observable()
                        .toList()
                        .toObservable();
            }

            @Override
            public Observable<Iterable<Source>> saveSourcesDefault(List<Source> sources) {
                return entityStore
                        .upsert(sources)
                        .toObservable();
            }

            @Override
            public Disposable saveSources(List<Source> sources) {
                return saveSourcesDefault(sources).subscribe();
            }

            @Override
            public Observable<List<Article>> getArticles(String sourceId) {
                return entityStore
                        .select(Article.class)
                        .where(ArticleEntity.SOURCE_ID.eq(sourceId))
                        .get()
                        .observable()
                        .toList()
                        .toObservable();
            }

            @Override
            public Observable<Iterable<Article>> saveArticlesDefault(List<Article> articles) {
                return entityStore
                        .upsert(articles)
                        .toObservable();
            }

            @Override
            public Disposable saveArticles(List<Article> articles) {
                return saveArticlesDefault(articles).subscribe();
            }
        };
    }

    @Provides
    @Singleton
    ReactiveEntityStore<Persistable> provideReactiveEntityStore(Context context) {
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
