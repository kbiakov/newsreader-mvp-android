package io.github.kbiakov.newsreader.models;

import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import io.requery.Entity;
import io.requery.Persistable;

@JsonObject @Entity
public abstract class Article implements Parcelable, Persistable {
    public @JsonField String author;
    public @JsonField String title;
    public @JsonField String description;
    public @JsonField String url;
    public @JsonField String urlToImage;
    public @JsonField String publishedAt;
}
