package io.github.kbiakov.newsreader.models.response;

import android.support.annotation.NonNull;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Collections;
import java.util.List;

@JsonObject
public abstract class Response<T> {
    @JsonField String status;

    private static final String STATUS_OK = "ok";

    Response() {
        this.status = STATUS_OK;
    }

    Response(Throwable t) {
        this.status = t.getLocalizedMessage();
    }

    public abstract List<T> getData() throws RuntimeException;

    private boolean isOk() {
        return status.equals(STATUS_OK);
    }

    void checkStatus() throws RuntimeException {
        if (!isOk()) {
            throw new RuntimeException(String.format("Cannot access data: %s", status));
        }
    }
}
