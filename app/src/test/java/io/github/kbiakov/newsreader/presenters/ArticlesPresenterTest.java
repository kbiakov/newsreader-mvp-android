package io.github.kbiakov.newsreader.presenters;

import org.junit.Test;

import java.util.List;

import io.github.kbiakov.newsreader.api.mocks.ArticlesMock;
import io.github.kbiakov.newsreader.api.mocks.IMock;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.models.response.SortBy;
import io.github.kbiakov.newsreader.screens.articles.ArticlesPresenter;
import io.github.kbiakov.newsreader.screens.articles.ArticlesView;
import io.reactivex.Observable;

import static io.github.kbiakov.newsreader.api.mocks.ArticlesMock.MOCK_ARTICLE_URL;
import static io.github.kbiakov.newsreader.api.mocks.ArticlesMock.MOCK_SOURCE_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ArticlesPresenterTest extends AbsPresenterTest<Article, ArticlesResponse, ArticlesView, ArticlesPresenter> {

    // - Mock

    @Override
    IMock<ArticlesResponse> createMock() {
        return new ArticlesMock();
    }

    @Override
    List<Article> createMockData() {
        return ArticlesMock.createData();
    }

    // - Interface

    @Override
    void togglePresenterForLoad() {
        presenter.loadArticles(false, MOCK_SOURCE_ID);
    }

    @Test
    @Override
    void testOnSomethingSelected() {
        presenter.onArticleSelected(MOCK_ARTICLE_URL);

        verify(view).showArticle(MOCK_ARTICLE_URL);
        verify(view, never()).showError(any(Throwable.class), false);
    }

    // - Data source

    @Override
    Observable<ArticlesResponse> makeApiRequest() {
        return api.getArticles(MOCK_SOURCE_ID, SortBy.TOP).toObservable();
    }

    @Override
    Observable<List<Article>> fetchFromDb() {
        return db.getArticles(MOCK_SOURCE_ID);
    }
}
