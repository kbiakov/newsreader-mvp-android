package io.github.kbiakov.newsreader.models.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class ArticleJson {
    public @JsonField String author;
    public @JsonField String title;
    public @JsonField String description;
    public @JsonField String url;
    public @JsonField String urlToImage;
    public @JsonField String publishedAt;
}
