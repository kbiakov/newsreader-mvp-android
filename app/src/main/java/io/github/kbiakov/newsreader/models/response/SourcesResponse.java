package io.github.kbiakov.newsreader.models.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

import io.github.kbiakov.newsreader.models.json.ArticleJson;

@JsonObject
public class SourcesResponse extends Response<ArticleJson> {
    @JsonField List<ArticleJson> sources;

    @Override
    public List<ArticleJson> getData() throws RuntimeException {
        checkStatus();
        return sources;
    }
}
