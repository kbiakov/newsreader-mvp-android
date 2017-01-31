package io.github.kbiakov.newsreader.db;

import java.util.ArrayList;
import java.util.List;

import io.github.kbiakov.newsreader.api.mocks.ArticlesMock;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.reactivex.Observable;

import static io.github.kbiakov.newsreader.api.mocks.ArticlesMock.MOCK_SOURCE_ID;

public class ArticlesDbTest extends DbTest<Article> {
    @Override
    List<Article> mockData() {
        return ArticlesMock.create();
    }

    @Override
    Observable<List<Article>> fetch() {
        return dbStore.getArticles(MOCK_SOURCE_ID);
    }
}
