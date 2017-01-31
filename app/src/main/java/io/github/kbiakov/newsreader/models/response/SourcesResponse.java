package io.github.kbiakov.newsreader.models.response;

import android.support.annotation.NonNull;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Collections;
import java.util.List;

import io.github.kbiakov.newsreader.models.json.SourceJson;

@JsonObject
public class SourcesResponse extends Response<SourceJson> {
    @JsonField List<SourceJson> sources;

    public SourcesResponse() {
        super();
        this.sources = Collections.emptyList();
    }

    private SourcesResponse(Throwable t) {
        super(t);
    }

    @Override
    public List<SourceJson> getData() throws RuntimeException {
        checkStatus();
        return sources;
    }

    @Override
    public void setData(List<SourceJson> data) throws RuntimeException {
        checkStatus();
        this.sources = data;
    }

    @NonNull
    public static SourcesResponse empty() {
        return new SourcesResponse();
    }

    @NonNull
    public static SourcesResponse invalid(Throwable t) {
        return new SourcesResponse(t);
    }
}
