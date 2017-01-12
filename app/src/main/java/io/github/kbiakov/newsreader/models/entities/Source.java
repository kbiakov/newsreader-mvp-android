package io.github.kbiakov.newsreader.models.entities;

import android.os.Parcelable;

import io.requery.Entity;
import io.requery.Key;
import io.requery.Persistable;

@Entity
public interface Source extends Parcelable, Persistable {
    @Key
    String getId();
    void setId(String id);

    String getName();
    void setName(String name);

    String getImageUrl();
    void setImageUrl(String imageUrl);
}
