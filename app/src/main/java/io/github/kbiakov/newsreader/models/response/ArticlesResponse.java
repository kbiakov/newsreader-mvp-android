package io.github.kbiakov.newsreader.models.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

import io.github.kbiakov.newsreader.models.json.ArticleJson;

@JsonObject
public class ArticlesResponse extends Response<ArticleJson> {
    @JsonField String source;
    @JsonField String sortBy;
    @JsonField List<ArticleJson> articles;

    @Override
    public List<ArticleJson> getData() throws RuntimeException {
        checkStatus();
        return articles;
    }
}
