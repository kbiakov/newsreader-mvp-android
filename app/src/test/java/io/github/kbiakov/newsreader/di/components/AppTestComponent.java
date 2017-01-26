package io.github.kbiakov.newsreader.di.components;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                /*
                AppTestModule.class,
                CacheTestModule.class,
                NetworkTestModule.class
                */
        }
)
public interface AppTestComponent {
    /*
    void inject(HomePresenterTest homePresenterTest);
    void inject(ArticlesPresenterTest articlesPresenterTest);
    */
}
