package io.github.kbiakov.newsreader.models.entities;

import android.os.Parcelable;

import io.github.kbiakov.newsreader.models.json.SourceJson;
import io.requery.Entity;
import io.requery.Key;
import io.requery.Persistable;

@Entity
public abstract class AbstractSource extends SourceJson implements Parcelable, Persistable {
    @Key public String id;
}
