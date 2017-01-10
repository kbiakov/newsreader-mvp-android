package io.github.kbiakov.newsreader.models.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

import io.github.kbiakov.newsreader.models.json.SourceJson;

@JsonObject
public class SourcesResponse extends Response<SourceJson> {
    @JsonField List<SourceJson> sources;

    @Override
    public List<SourceJson> getData() throws RuntimeException {
        checkStatus();
        return sources;
    }
}
