package io.github.kbiakov.newsreader.di.components;

import javax.inject.Singleton;

import dagger.Component;
import io.github.kbiakov.newsreader.di.modules.AppModule;
import io.github.kbiakov.newsreader.di.modules.CacheModule;
import io.github.kbiakov.newsreader.di.modules.NetworkModule;
import io.github.kbiakov.newsreader.screens.articles.ArticlesPresenter;
import io.github.kbiakov.newsreader.screens.home.HomePresenter;

@Singleton
@Component(
        modules = {
                AppModule.class,
                CacheModule.class,
                NetworkModule.class
        }
)
public interface AppComponent {
    void inject(HomePresenter homePresenter);
    void inject(ArticlesPresenter articlesPresenter);
}
