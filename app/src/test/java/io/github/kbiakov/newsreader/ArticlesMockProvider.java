package io.github.kbiakov.newsreader;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.kbiakov.newsreader.api.ApiService;
import io.github.kbiakov.newsreader.db.DbStore;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.entities.ArticleEntity;
import io.github.kbiakov.newsreader.models.json.ArticleJson;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.models.response.SortBy;
import io.reactivex.Observable;

class ArticlesMockProvider implements MockProvider<Article, ArticlesResponse> {

    public static final String MOCK_SOURCE_ID = "the-next-web";
    public static final int MOCK_ARTICLES_COUNT = 10;
    public static final String MOCK_ARTICLE_URL = "http://thenextweb.com/insider/2017/01/20/director-robbed-grocery-store-piracy/";

    private ApiService apiService;
    private DbStore dbStore;

    ArticlesMockProvider(ApiService apiService, DbStore dbStore) {
        this.apiService = apiService;
        this.dbStore = dbStore;
    }

    // - Data source

    @Override
    public Observable<ArticlesResponse> getFromNetwork() {
        return apiService.getArticles(MOCK_SOURCE_ID, SortBy.TOP);
    }

    @Override
    public Observable<ArticlesResponse> noInternetError() {
        return Observable.error(new UnknownHostException());
    }

    @Override
    public Observable<List<Article>> getFromDb() {
        return dbStore.getArticles(MOCK_SOURCE_ID);
    }

    @Override
    public Observable<Iterable<Article>> saveToDb() {
        return dbStore.saveArticlesDefault(mockData());
    }

    @Override
    public Observable<List<Article>> getFromAnywhere() {
        return getFromNetwork().concatWith(getFromDb());
    }

    // - Data

    @Override
    public List<Article> mockData() {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < MOCK_ARTICLES_COUNT; i++) {
            articles.add(new ArticleEntity());
        }
        return articles;
    }

    @Override
    public List<Article> emptyData() {
        return Collections.emptyList();
    }

    // - Response

    @Override
    public ArticlesResponse mockResponse() {
        List<ArticleJson> articlesJson = new ArrayList<>();
        for (int i = 0; i < MOCK_ARTICLES_COUNT; i++) {
            articlesJson.add(new ArticleJson());
        }

        ArticlesResponse res = new ArticlesResponse();
        res.setData(articlesJson);
        return res;
    }

    @Override
    public ArticlesResponse emptyResponse() {
        return ArticlesResponse.empty();
    }

    @Override
    public ArticlesResponse invalidResponse() {
        return ArticlesResponse.invalid(new RuntimeException("Mocked invalid response."));
    }
}
