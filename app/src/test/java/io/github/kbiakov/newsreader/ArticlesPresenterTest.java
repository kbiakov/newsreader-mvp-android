package io.github.kbiakov.newsreader;

import org.junit.Test;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.entities.ArticleEntity;
import io.github.kbiakov.newsreader.models.json.ArticleJson;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.models.response.SortBy;
import io.github.kbiakov.newsreader.screens.articles.ArticlesPresenter;
import io.github.kbiakov.newsreader.screens.articles.ArticlesView;
import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ArticlesPresenterTest extends MvpPresenterTest<ArticlesView, ArticlesPresenter>
        implements MockProvider<Article, ArticlesResponse> {

    // - Interface tests

    @Test
    void testLoadArticlesByInit() {
        testLoadArticles(false);
    }

    @Test
    void testLoadArticlesByRefreshing() {
        testLoadArticles(true);
    }

    private void testLoadArticles(boolean pullToRefresh) {
        when(getFromNetwork()).thenReturn(Observable.create(s -> {
            s.onNext(mockResponse());
            s.onComplete();
        }));

        when(getFromDb()).thenReturn(Observable.create(s -> {
            s.onNext(mockData());
            s.onComplete();
        }));

        presenter.loadArticles(pullToRefresh, MOCK_SOURCE_ID);

        verify(view).showLoading(pullToRefresh);
        verify(view).setData(mockData());
        verify(view, never()).showError(any(Throwable.class), false);
    }

    @Test
    void testOnArticleSelected() {
        presenter.onArticleSelected(MOCK_ARTICLE_URL);
        verify(view).showArticle(MOCK_ARTICLE_URL);
        verify(view, never()).showError(any(Throwable.class), true);
    }

    // - Data source tests

    @Override
    public void getFromNetwork_Success() {
        when(getFromNetwork()).thenReturn(Observable.create(s -> {
            s.onNext(mockResponse());
            s.onComplete();
        }));
    }

    @Override
    public void getFromNetwork_NoInternet() {
        when(getFromNetwork()).thenReturn(noInternetError());

        // TODO
    }

    @Override
    public void getFromNetwork_Empty() {
        when(getFromNetwork()).thenReturn(Observable.create(s -> {
            s.onNext(emptyResponse());
            s.onComplete();
        }));

        // TODO
    }

    @Override
    public void getFromNetwork_Fail() {
        when(getFromNetwork()).thenReturn(Observable.create(s -> {
            s.onNext(invalidResponse());
            s.onComplete();
        }));

        // TODO
    }

    @Override
    public void getFromDb_Success() {
        when(getFromDb()).thenReturn(Observable.create(s -> {
            s.onNext(mockData());
            s.onComplete();
        }));

        // TODO
    }

    @Override
    public void getFromDb_Empty() {
        when(getFromDb()).thenReturn(Observable.create(s -> {
            s.onNext(emptyData());
            s.onComplete();
        }));

        // TODO
    }

    @Override
    public void saveToDb_Success() {
        when(dbStore.saveArticles(mockData()));

        // TODO
    }

    @Override
    public void getFromAnywhere_Success() {
        // TODO
    }

    @Override
    public void getFromAnywhere_Empty() {
        // TODO
    }

    // - Mock provider

    @Override
    public Observable<ArticlesResponse> getFromNetwork() {
        return apiService.getArticles(MOCK_SOURCE_ID, SortBy.TOP);
    }

    @Override
    public Observable<List<Article>> getFromDb() {
        return dbStore.getArticles(MOCK_SOURCE_ID);
    }

    @Override
    public ArticlesResponse mockResponse() {
        ArticlesResponse res = new ArticlesResponse();
        List<ArticleJson> articlesJson = new ArrayList<>();
        for (int i = 0; i < MOCK_ITEMS_COUNT; i++) {
            articlesJson.add(new ArticleJson());
        }
        res.setData(articlesJson);
        return res;
    }

    @Override
    public ArticlesResponse emptyResponse() {
        return ArticlesResponse.empty();
    }

    @Override
    public ArticlesResponse invalidResponse() {
        return ArticlesResponse.invalid(any(Throwable.class));
    }

    @Override
    public List<Article> mockData() {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < MOCK_ITEMS_COUNT; i++) {
            articles.add(new ArticleEntity());
        }
        return articles;
    }

    @Override
    public List<Article> emptyData() {
        return Collections.emptyList();
    }

    // - Method aliases

    private Observable<ArticlesResponse> noInternetError() {
        return Observable.error(new UnknownHostException());
    }

    /*
    @Override
    public void performGetFromNetwork_success() {

        when(apiService.getArticles(sourceId, null))
                .thenReturn(Observable.create(s -> {
                    s.onNext(ArticlesResponse.empty());
                    s.onComplete();
                }));

        when(apiService.getArticles(sourceId, null)).thenReturn(noInternetError());

        presenter.getFromNetwork(sourceId);

        //verify(logger).print("ABC");
    }

    @Override
    public void performGetFromNetwork_failed_noInternet() {
        String sourceId = "";

        when(apiService.getArticles(sourceId, null))
                .thenReturn(Observable.create(s -> {
                    s.onNext(ArticlesResponse.empty());
                    s.onComplete();
                }));

        when(apiService.getArticles(sourceId, null)).thenReturn(noInternetError());

        presenter.getFromNetwork(sourceId);

        //verify(logger).print("ABC");
    }
    */
}
