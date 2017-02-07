package io.github.kbiakov.newsreader.presenters;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import io.github.kbiakov.newsreader.api.ApiService;
import io.github.kbiakov.newsreader.mock.ArticlesMock;
import io.github.kbiakov.newsreader.api.providers.ArticlesProvider;
import io.github.kbiakov.newsreader.db.DbStore;
import io.github.kbiakov.newsreader.db.NoDataException;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.models.response.SortBy;
import io.github.kbiakov.newsreader.screens.articles.ArticlesPresenter;
import io.github.kbiakov.newsreader.screens.articles.ArticlesView;
import io.reactivex.Observable;
import io.reactivex.Single;

import static io.github.kbiakov.newsreader.mock.ArticlesMock.MOCK_ARTICLE_URL;
import static io.github.kbiakov.newsreader.mock.ArticlesMock.MOCK_SOURCE_ID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ArticlesPresenterTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock ApiService api;
    @InjectMocks ArticlesProvider provider;
    @Mock DbStore db;
    @Mock ArticlesView view;
    @InjectMocks ArticlesPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void attachView() {
        presenter.attachView(view);
    }

    @After
    public void detachView() {
        presenter.detachView(true);
    }

    // - Interface

    @Test
    public void testLoadArticles() {
        when(makeApiRequest()).thenReturn(
                Single.just(new ArticlesResponse())
        );
        when(fetchFromDb()).thenReturn(
                Observable.just(ArticlesMock.createData())
        );

        presenter.loadArticles(false, MOCK_SOURCE_ID);

        verify(view).showLoading(false);
        verify(view).setData(ArticlesMock.createData());
        verify(view, never()).showError(any(Throwable.class), false);
    }

    @Test
    public void testOnArticlesSelected() {
        presenter.onArticleSelected(MOCK_ARTICLE_URL);

        verify(view).showArticle(MOCK_ARTICLE_URL);
        verify(view, never()).showError(any(Throwable.class), false);
    }

    // - Test cases

    @Test
    public void testLoadArticles_NoInternet() {
        when(makeApiRequest()).thenReturn(
                Single.error(new UnknownHostException())
        );
        when(fetchFromDb()).thenReturn(
                Observable.just(ArticlesMock.createData())
        );

        presenter.loadArticles(false, MOCK_SOURCE_ID);

        verify(view).showLoading(false);
        verify(view).setData(ArticlesMock.createData());
        verify(view, never()).showError(any(Throwable.class), false);
    }

    @Test
    public void testLoadArticles_NoData() {
        when(makeApiRequest()).thenReturn(
                Single.error(new UnknownHostException())
        );
        when(fetchFromDb()).thenReturn(
                Observable.just(Collections.emptyList())
        );

        presenter.loadArticles(false, MOCK_SOURCE_ID);

        verify(view).showLoading(false);
        verify(view, never()).setData(any());
        verify(view).showError(NoDataException.create(), false);
    }

    @Test
    public void testLoadArticles_InternetError() {
        when(makeApiRequest()).thenReturn(
                Single.error(new RuntimeException("Something went wrong"))
        );
        when(fetchFromDb()).thenReturn(
                Observable.just(ArticlesMock.createData())
        );

        presenter.loadArticles(false, MOCK_SOURCE_ID);

        verify(view).showLoading(false);
        verify(view, never()).setData(ArticlesMock.createData());
        verify(view).showError(any(Throwable.class), false);
    }

    @Test
    public void testLoadArticles_DbError() {
        when(makeApiRequest()).thenReturn(
                Single.just(new ArticlesResponse())
        );
        when(fetchFromDb()).thenReturn(
                Observable.error(new RuntimeException("Something went wrong"))
        );

        presenter.loadArticles(false, MOCK_SOURCE_ID);

        verify(view).showLoading(false);
        verify(view, never()).setData(ArticlesMock.createData());
        verify(view).showError(any(Throwable.class), false);
    }

    // - Data source

    private Single<ArticlesResponse> makeApiRequest() {
        return api.getArticles(MOCK_SOURCE_ID, SortBy.TOP);
    }

    private Observable<List<Article>> fetchFromDb() {
        return db.getArticles(MOCK_SOURCE_ID);
    }
}
