package io.github.kbiakov.newsreader.models.json;

import android.support.annotation.Nullable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class SourceJson {
    public @JsonField String id;
    public @JsonField String name;
    public @JsonField String description;
    public @JsonField String url;
    public @JsonField String category;
    public @JsonField String language;
    public @JsonField String country;
    public @JsonField ImageUrl urlsToLogos;
    public @JsonField List<String> sortBysAvailable;

    @Nullable
    public String getImageUrl() {
        return urlsToLogos.getUrl();
    }

    @JsonObject
    public static class ImageUrl {
        public @JsonField String small;
        public @JsonField String medium;
        public @JsonField String large;

        @Nullable
        public String getUrl() {
            return large != null ? large : medium != null ? medium : small;
        }
    }
}
