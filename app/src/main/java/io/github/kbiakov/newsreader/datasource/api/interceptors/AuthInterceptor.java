package io.github.kbiakov.newsreader.datasource.api.interceptors;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private static final String PARAM_API_KEY = "apiKey";

    private String apiKey;

    public AuthInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        HttpUrl url = chain.request().url().newBuilder()
                .addQueryParameter(PARAM_API_KEY, apiKey)
                .build();

        Request request = chain.request().newBuilder()
                .url(url)
                .build();

        return chain.proceed(request);
    }
}
