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

import io.github.kbiakov.newsreader.api.mocks.ArticlesMock;
import io.github.kbiakov.newsreader.api.mocks.IMock;
import io.github.kbiakov.newsreader.api.providers.ArticlesProvider;
import io.github.kbiakov.newsreader.db.DbStore;
import io.github.kbiakov.newsreader.db.NoDataException;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.screens.articles.ArticlesPresenter;
import io.github.kbiakov.newsreader.screens.articles.ArticlesView;
import io.reactivex.Observable;

import static io.github.kbiakov.newsreader.api.mocks.ArticlesMock.MOCK_ARTICLE_URL;
import static io.github.kbiakov.newsreader.api.mocks.ArticlesMock.MOCK_SOURCE_ID;
import static io.reactivex.Observable.error;
import static io.reactivex.Observable.just;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ArticlesPresenterTest {

    // - Mock

    private IMock<ArticlesResponse> createMock() {
        return new ArticlesMock();
    }

    private List<Article> createMockData() {
        return ArticlesMock.createData();
    }

    // - Data source

    private Observable<List<Article>> makeApiRequest() {
        return api.getArticles(MOCK_SOURCE_ID);
    }

    private Observable<List<Article>> fetchFromDb() {
        return db.getArticles(MOCK_SOURCE_ID);
    }

    // - Main

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock public ArticlesProvider api;
    @Mock public DbStore db;
    @Mock public ArticlesView view;
    @InjectMocks public ArticlesPresenter presenter;

    private final IMock<ArticlesResponse> mock = createMock();

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
    public void testLoadSomething() {
        when(makeApiRequest()).thenReturn(
                just(createMockData())
        );
        when(fetchFromDb()).thenReturn(
                just(createMockData())
        );

        loadArticles();

        verify(view).showLoading(false);
        verify(view).setData(createMockData());
        verify(view, never()).showError(any(Throwable.class), false);
    }

    // - Test cases

    @Test
    public void testLoad_NoInternet() {
        when(makeApiRequest()).thenReturn(
                error(new UnknownHostException())
        );
        when(fetchFromDb()).thenReturn(
                just(createMockData())
        );

        loadArticles();

        verify(view).showLoading(false);
        verify(view).setData(createMockData());
        verify(view, never()).showError(any(Throwable.class), false);
    }

    @Test
    public void testLoadSources_NoData() {
        when(makeApiRequest()).thenReturn(
                error(new UnknownHostException())
        );
        when(fetchFromDb()).thenReturn(
                just(Collections.emptyList())
        );

        loadArticles();

        verify(view).showLoading(false);
        verify(view, never()).setData(any());
        verify(view).showError(NoDataException.create(), false);
    }

    @Test
    public void testLoadArticles_InternetError() {
        when(makeApiRequest()).thenReturn(
                error(new RuntimeException("Something goes wrong."))
        );
        when(fetchFromDb()).thenReturn(
                just(createMockData())
        );

        loadArticles();

        verify(view).showLoading(false);
        verify(view, never()).setData(createMockData());
        verify(view).showError(any(Throwable.class), false);
    }

    @Test
    public void testLoadArticles_DbError() {
        when(makeApiRequest()).thenReturn(
                just(createMockData())
        );
        when(fetchFromDb()).thenReturn(
                error(new RuntimeException("Something goes wrong."))
        );

        loadArticles();

        verify(view).showLoading(false);
        verify(view, never()).setData(createMockData());
        verify(view).showError(any(Throwable.class), false);
    }

    private void loadArticles() {
        presenter.loadArticles(false, MOCK_SOURCE_ID);
    }

    @Test
    public void testOnArticlesSelected() {
        presenter.onArticleSelected(MOCK_ARTICLE_URL);

        verify(view).showArticle(MOCK_ARTICLE_URL);
        verify(view, never()).showError(any(Throwable.class), false);
    }
}
