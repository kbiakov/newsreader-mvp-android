package io.github.kbiakov.newsreader.datasource;

import java.util.ArrayList;
import java.util.List;

import io.github.kbiakov.newsreader.datasource.api.Api;
import io.github.kbiakov.newsreader.datasource.api.ApiService;
import io.github.kbiakov.newsreader.datasource.db.DbManager;
import io.github.kbiakov.newsreader.models.Article;
import io.github.kbiakov.newsreader.models.Source;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class DataSource {

    public static ApiService api() {
        return Api.getInstance().getApiService();
    }

    public static ReactiveEntityStore<Persistable> db() {
        return DbManager.getInstance().getData();
    }

    public static List<Source> emptySources() {
        return new ArrayList<>();
    }

    public static List<Article> emptyArticles() {
        return new ArrayList<>();
    }
}