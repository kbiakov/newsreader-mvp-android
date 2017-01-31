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

import java.util.List;

import io.github.kbiakov.newsreader.api.ApiService;
import io.github.kbiakov.newsreader.api.mocks.ArticlesMock;
import io.github.kbiakov.newsreader.db.DbStore;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.models.response.SortBy;
import io.github.kbiakov.newsreader.screens.articles.ArticlesPresenter;
import io.github.kbiakov.newsreader.screens.articles.ArticlesView;
import io.reactivex.Observable;

import static io.github.kbiakov.newsreader.api.mocks.ArticlesMock.MOCK_ARTICLE_URL;
import static io.github.kbiakov.newsreader.api.mocks.ArticlesMock.MOCK_SOURCE_ID;
import static io.reactivex.Observable.just;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ArticlesPresenterTest {

    @Rule MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock ApiService api;
    @Mock DbStore db;
    @Mock ArticlesView view;
    @InjectMocks ArticlesPresenter presenter;

    private final ArticlesMock mock = new ArticlesMock();

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

    // - Interface tests

    @Test
    void testLoadArticles() {
        when(makeApiRequest()).thenReturn(
                just(mock.mockResponse())
        );
        when(fetchFromDb()).thenReturn(
                just(ArticlesMock.create())
        );

        presenter.loadArticles(false, MOCK_SOURCE_ID);

        verify(view).showLoading(false);
        verify(view).setData(ArticlesMock.create());
        verify(view, never()).showError(any(Throwable.class), false);
    }

    @Test
    void testOnArticleSelected() {
        presenter.onArticleSelected(MOCK_ARTICLE_URL);

        verify(view).showArticle(MOCK_ARTICLE_URL);
        verify(view, never()).showError(any(Throwable.class), false);
    }

    // - Test cases

    // - Data source

    private Observable<ArticlesResponse> makeApiRequest() {
        return api.getArticles(MOCK_SOURCE_ID, SortBy.TOP).toObservable();
    }

    private Observable<List<Article>> fetchFromDb() {
        return db.getArticles(MOCK_SOURCE_ID);
    }
}
