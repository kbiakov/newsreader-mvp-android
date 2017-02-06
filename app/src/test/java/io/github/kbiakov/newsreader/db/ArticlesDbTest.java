package io.github.kbiakov.newsreader.db;

import java.util.List;

import io.github.kbiakov.newsreader.mock.ArticlesMock;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.reactivex.Observable;

import static io.github.kbiakov.newsreader.mock.ArticlesMock.MOCK_SOURCE_ID;

public class ArticlesDbTest extends AbsDbTest<Article> {
    @Override
    List<Article> mockData() {
        return ArticlesMock.createData();
    }

    @Override
    Observable<List<Article>> fetch() {
        return dbStore.getArticles(MOCK_SOURCE_ID);
    }
}
