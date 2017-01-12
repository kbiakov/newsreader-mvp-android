package io.github.kbiakov.newsreader.models.json;

import android.support.annotation.Nullable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class ImageUrlJson {
    @JsonField public String small;
    @JsonField public String medium;
    @JsonField public String large;

    @Nullable
    public String getUrl() {
        return large != null ? large : medium != null ? medium : small;
    }
}
