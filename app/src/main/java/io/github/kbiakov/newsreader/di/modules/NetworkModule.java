package io.github.kbiakov.newsreader.di.modules;

import android.content.Context;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.kbiakov.newsreader.api.ApiService;
import io.github.kbiakov.newsreader.api.interceptors.AuthInterceptor;
import io.github.kbiakov.newsreader.api.interceptors.LoggingInterceptor;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Module
public class NetworkModule {

    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 45;
    private static final int TIMEOUT = 30;

    @Provides
    @Singleton
    ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(ApiService.API_ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(LoganSquareConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(ApiService.API_KEY))
                .addNetworkInterceptor(new LoggingInterceptor())
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    Cache provideCache(Context context) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        return cache;
    }
}
