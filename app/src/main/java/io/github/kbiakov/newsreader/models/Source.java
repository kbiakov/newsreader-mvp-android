package io.github.kbiakov.newsreader.models;

import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

import io.requery.Entity;
import io.requery.Key;
import io.requery.Persistable;

@JsonObject @Entity
public abstract class Source implements Parcelable, Persistable {
    public @JsonField @Key String id;
    public @JsonField String name;
    public @JsonField String description;
    public @JsonField String url;
    public @JsonField String category;
    public @JsonField String language;
    public @JsonField String country;
    public @JsonField ImageUrl urlsToLogos;
    public @JsonField List<String> sortBysAvailable;

    @JsonObject @Entity
    public static abstract class ImageUrl implements Parcelable, Persistable {
        public @JsonField String small;
        public @JsonField String medium;
        public @JsonField String large;
    }
}
