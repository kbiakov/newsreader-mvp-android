package io.github.kbiakov.newsreader.models.entities;

import android.os.Parcelable;

import io.requery.Entity;
import io.requery.Key;
import io.requery.Persistable;

@Entity
public interface Article extends Parcelable, Persistable {
    @Key
    String getSourceId();
    void setSourceId(String sourceId);

    String getAuthor();
    void setAuthor(String author);

    String getTitle();
    void setTitle(String title);

    String getDescription();
    void setDescription(String description);

    String getUrl();
    void setUrl(String url);

    String getUrlToImage();
    void setUrlToImage(String urlToImage);
}
