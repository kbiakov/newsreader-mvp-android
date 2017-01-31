package io.github.kbiakov.newsreader.api.providers;

import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import io.github.kbiakov.newsreader.api.ApiService;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.json.ArticleJson;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.models.response.SortBy;
import io.reactivex.Observable;

public class ArticlesProvider {

    private ApiService service;

    @Inject
    ArticlesProvider(ApiService service) {
        this.service = service;
    }

    public Observable<List<Article>> getArticles(String sourceId) {
        return service
                .getArticles(sourceId, SortBy.TOP)
                .onErrorReturn(t -> {
                    if (t instanceof UnknownHostException) { // no internet
                        return ArticlesResponse.empty();
                    }
                    return ArticlesResponse.invalid(t);
                })
                .map(ArticlesResponse::getData)
                .map(a -> ArticleJson.asEntities(a, sourceId))
                .toObservable();
    }
}
