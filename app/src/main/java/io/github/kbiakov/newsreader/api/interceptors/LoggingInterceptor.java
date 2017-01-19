package io.github.kbiakov.newsreader.api.interceptors;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

public class LoggingInterceptor implements Interceptor {

    private static final String TAG = "LoggingInterceptor";

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.i(TAG, String.format("Sending request %s on %s%n%s\nBody: %s",
                request.url(), chain.connection(), request.headers(), bodyToString(request)));

        request.body();

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.i(TAG, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }

    private static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException | NullPointerException e) {
            return e.getLocalizedMessage();
        }
    }
}
