package io.github.kbiakov.newsreader;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.github.kbiakov.newsreader.di.components.AppComponent;
import io.github.kbiakov.newsreader.di.components.DaggerAppComponent;
import io.github.kbiakov.newsreader.di.modules.AppModule;
import io.github.kbiakov.newsreader.di.modules.CacheModule;
import io.github.kbiakov.newsreader.di.modules.NetworkModule;

public class App extends Application {

    private static AppComponent appComponent;

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = buildComponent();

        refWatcher = LeakCanary.install(this);
        StrictMode.enableDefaults();
        Fresco.initialize(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .cacheModule(new CacheModule())
                .build();
    }
}
