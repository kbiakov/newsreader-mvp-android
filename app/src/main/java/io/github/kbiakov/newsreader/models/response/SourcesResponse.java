package io.github.kbiakov.newsreader.models.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

import io.github.kbiakov.newsreader.models.entities.Source;

@JsonObject
public class SourcesResponse extends Response<Source> {
    @JsonField List<Source> sources;

    @Override
    public List<Source> getData() throws RuntimeException {
        checkStatus();
        return sources;
    }
}
