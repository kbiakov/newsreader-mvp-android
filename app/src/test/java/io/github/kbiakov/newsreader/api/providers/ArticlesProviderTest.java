package io.github.kbiakov.newsreader.api.providers;

import io.github.kbiakov.newsreader.api.mocks.ArticlesMock;
import io.github.kbiakov.newsreader.api.mocks.IMock;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.models.response.SortBy;
import io.reactivex.Single;

import static io.github.kbiakov.newsreader.api.mocks.ArticlesMock.MOCK_SOURCE_ID;

public class ArticlesProviderTest extends ProviderTest<ArticlesResponse> {
    @Override
    IMock<ArticlesResponse> createMock() {
        return new ArticlesMock();
    }

    @Override
    Single<ArticlesResponse> apiRequest() {
        return apiService.getArticles(MOCK_SOURCE_ID, SortBy.TOP);
    }
}
