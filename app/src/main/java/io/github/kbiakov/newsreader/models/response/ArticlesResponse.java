package io.github.kbiakov.newsreader.models.response;

import android.support.annotation.NonNull;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Collections;
import java.util.List;

import io.github.kbiakov.newsreader.models.json.ArticleJson;

@JsonObject
public class ArticlesResponse extends Response<ArticleJson> {
    @JsonField String source;
    @JsonField String sortBy;
    @JsonField List<ArticleJson> articles;

    public ArticlesResponse() {
        super();
        this.source = "";
        this.sortBy = SortBy.TOP;
        this.articles = Collections.emptyList();
    }

    private ArticlesResponse(Throwable t) {
        super(t);
    }

    @Override
    public List<ArticleJson> getData() throws RuntimeException {
        checkStatus();
        return articles;
    }

    @Override
    public void setData(List<ArticleJson> data) throws RuntimeException {
        checkStatus();
        this.articles = data;
    }

    @NonNull
    public static ArticlesResponse empty() {
        return new ArticlesResponse();
    }

    @NonNull
    public static ArticlesResponse invalid(Throwable t) {
        return new ArticlesResponse(t);
    }
}
