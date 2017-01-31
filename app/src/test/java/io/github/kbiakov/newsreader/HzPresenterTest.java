package io.github.kbiakov.newsreader;

import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.List;

import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.screens.articles.ArticlesPresenter;
import io.github.kbiakov.newsreader.screens.articles.ArticlesView;
import io.reactivex.observers.TestObserver;

import static io.github.kbiakov.newsreader.ArticlesMockProvider.*;
import static io.reactivex.Observable.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HzPresenterTest extends MvpPresenterTest<ArticlesView, ArticlesPresenter> {

    private ArticlesMockProvider mock;

    @Override
    public void setUp() {
        super.setUp();
        mock = new ArticlesMockProvider(apiService, dbStore);
    }

    @Override
    void getFromNetwork_Success() {
        // ???
    }

    @Before
    public void mockBeforeTest() {
        when(mock.getFromNetwork()).thenReturn(
                just(mock.mockResponse())
        );
        when(mock.getFromDb()).thenReturn(
                just(mock.mockData())
        );
    }

    private void testAfterFetchShouldPass() {
        presenter.loadArticles(false, MOCK_SOURCE_ID);

        verify(view).showLoading(false);
        verify(view).setData(mock.mockData());
        verify(view, never()).showError(any(Throwable.class), false);
    }

    private void testAfterFetchShouldFail(Throwable t) {
        presenter.loadArticles(false, MOCK_SOURCE_ID);

        verify(view).showLoading(false);
        verify(view, never()).setData(any());
        verify(view).showError(t, false);
    }

    // - Interface tests

    // api(1) db(1)
    @Test
    void testLoadArticles() {
        TestObserver<ArticlesResponse> testObserver = mock.getFromNetwork().test();
        testObserver.awaitTerminalEvent();
        testObserver
                .assertNoErrors()
                .assertValue(l -> l.getData().size() == MOCK_ARTICLES_COUNT);

        testAfterFetchShouldPass();
    }

    @Test
    void testOnArticleSelected() {
        presenter.onArticleSelected(MOCK_ARTICLE_URL);

        verify(view).showArticle(MOCK_ARTICLE_URL);
        verify(view, never()).showError(any(Throwable.class), false);
    }

    // - Data source tests

    // api(0) db(1)
    @Override
    public void getFromNetwork_Empty() {
        when(mock.getFromNetwork()).thenReturn(
                just(mock.emptyResponse())
        );

        TestObserver<ArticlesResponse> testObserver = mock.getFromNetwork().test();
        testObserver.awaitTerminalEvent();
        testObserver
                .assertNoErrors()
                .assertValue(l -> l.getData().isEmpty());

        testAfterFetchShouldPass();
    }

    // api(0) db(1)
    @Override
    public void getFromNetwork_NoInternet() {
        when(mock.getFromNetwork()).thenReturn(
                mock.noInternetError()
        );

        TestObserver<ArticlesResponse> testObserver = mock.getFromNetwork().test();
        testObserver.awaitTerminalEvent();
        testObserver.assertError(UnknownHostException.class);

        testAfterFetchShouldPass();
    }

    // api(-1) db(1)
    @Override
    public void getFromNetwork_Fail() {
        when(mock.getFromNetwork()).thenReturn(
                just(mock.invalidResponse())
        );

        TestObserver<ArticlesResponse> testObserver = mock.getFromNetwork().test();
        testObserver.awaitTerminalEvent();
        testObserver.assertError(any(Throwable.class));

        testAfterFetchShouldFail(new RuntimeException());
    }

    // api(1) db(1)
    @Override
    public void getFromDb_Success() {
        TestObserver<List<Article>> testObserver = mock.getFromDb().test();
        testObserver.awaitTerminalEvent();
        testObserver
                .assertNoErrors()
                .assertValue(l -> l.size() == MOCK_ARTICLES_COUNT);

        testAfterFetchShouldPass();
    }

    // api(1) db(0)
    @Override
    public void getFromDb_Empty() {
        when(mock.getFromDb()).thenReturn(
                just(mock.emptyData())
        );

        TestObserver<List<Article>> testObserver = mock.getFromDb().test();
        testObserver.awaitTerminalEvent();
        testObserver
                .assertNoErrors()
                .assertValue(List::isEmpty);

        testAfterFetchShouldPass();
    }

    @Override
    public void saveToDb_Success() {
        TestObserver<Iterable<Article>> testObserver = mock.saveToDb().test();
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();

        testAfterFetchShouldPass();
    }

    // api(0) db(0)
    @Override
    void getFromNetworkAndDb_NoInternetAndEmptyCache() {
        when(mock.getFromNetwork()).thenReturn(
                mock.noInternetError()
        );
        when(mock.getFromDb()).thenReturn(
                just(mock.emptyData())
        );

        testAfterFetchShouldFail(null);
    }

    // api(0) db(0)
    @Override
    void getFromNetworkAndDb_Empty() {
        when(mock.getFromNetwork()).thenReturn(
                just(mock.emptyResponse())
        );
        when(mock.getFromDb()).thenReturn(
                just(mock.emptyData())
        );

        testAfterFetchShouldFail(null);
    }
}
