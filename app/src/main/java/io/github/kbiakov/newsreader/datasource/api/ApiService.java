package io.github.kbiakov.newsreader.datasource.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.models.response.SourcesResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    String API_ENDPOINT = "https://newsapi.org/v1/";
    String API_KEY = "0906c46cc8ce4606af02aac8c1408139";

    @GET("sources")
    Observable<SourcesResponse> getSources(
            @Query("category") @Nullable String category,
            @Query("language") @Nullable String language,
            @Query("country") @Nullable String country);

    @GET("articles")
    Observable<ArticlesResponse> getArticles(
            @Query("source") @NonNull String source,
            @Query("sortBy") @Nullable String sortBy);
}
