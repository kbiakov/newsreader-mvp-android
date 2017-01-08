package io.github.kbiakov.newsreader.models.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
abstract class Response<T> {
    @JsonField String status;

    public abstract List<T> getData() throws RuntimeException;

    void checkStatus() throws RuntimeException {
        if (!isOk()) {
            throw new RuntimeException(String.format("Cannot access data: %s", status));
        }
    }

    private boolean isOk() {
        return status.equals("ok");
    }
}
