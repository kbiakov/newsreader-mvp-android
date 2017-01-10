package io.github.kbiakov.newsreader.models.entities;

import android.os.Parcelable;

import io.github.kbiakov.newsreader.models.json.ArticleJson;
import io.requery.Entity;
import io.requery.Key;
import io.requery.Persistable;

@Entity
public abstract class AbstractArticle extends ArticleJson implements Parcelable, Persistable {
    @Key public String url;
}
