package io.github.kbiakov.newsreader.datasource.api;

import android.support.annotation.NonNull;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.github.kbiakov.newsreader.datasource.api.interceptors.AuthInterceptor;
import io.github.kbiakov.newsreader.datasource.api.interceptors.LoggingInterceptor;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class Api {

    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 45;
    private static final int TIMEOUT = 30;

    private static volatile Api instance;
    private final OkHttpClient CLIENT;

    private Api() {
        CLIENT = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(ApiService.API_KEY))
                .addNetworkInterceptor(new LoggingInterceptor())
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public static Api getInstance() {
        Api localInstance = instance;

        if (localInstance == null) {
            synchronized (Api.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Api();
                }
            }
        }
        return localInstance;
    }

    @NonNull
    public ApiService getApiService() {
        return getRetrofit().create(ApiService.class);
    }

    @NonNull
    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(ApiService.API_ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(LoganSquareConverterFactory.create())
                .client(CLIENT)
                .build();
    }
}
