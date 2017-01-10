package io.github.kbiakov.newsreader.models.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

import io.github.kbiakov.newsreader.models.entities.Article;

@JsonObject
public class ArticlesResponse extends Response<Article> {
    @JsonField String source;
    @JsonField String sortBy;
    @JsonField List<Article> articles;

    @Override
    public List<Article> getData() throws RuntimeException {
        checkStatus();
        return articles;
    }
}
