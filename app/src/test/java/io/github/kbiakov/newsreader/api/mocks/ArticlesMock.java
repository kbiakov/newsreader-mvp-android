package io.github.kbiakov.newsreader.api.mocks;

import java.util.ArrayList;
import java.util.List;

import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.entities.ArticleEntity;
import io.github.kbiakov.newsreader.models.json.ArticleJson;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;

public class ArticlesMock implements IMock<ArticlesResponse> {

    public static final String MOCK_SOURCE_ID = "the-next-web";
    private static final int MOCK_ARTICLES_COUNT = 10;
    public static final String MOCK_ARTICLE_URL = "http://thenextweb.com/insider/2017/01/20/director-robbed-grocery-store-piracy/";

    @Override
    public ArticlesResponse mockResponse() {
        ArticlesResponse res = new ArticlesResponse();
        res.setData(createJsons());
        return res;
    }

    private static List<ArticleJson> createJsons() {
        List<ArticleJson> articleJsons = new ArrayList<>();
        for (int i = 0; i < MOCK_ARTICLES_COUNT; i++) {
            articleJsons.add(new ArticleJson());
        }
        return articleJsons;
    }

    public static List<Article> createData() {
        return ArticleJson.asEntities(createJsons(), MOCK_SOURCE_ID);
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
