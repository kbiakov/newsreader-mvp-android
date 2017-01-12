package io.github.kbiakov.newsreader.models.json;

import android.support.annotation.NonNull;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

import io.github.kbiakov.newsreader.models.entities.Source;
import io.github.kbiakov.newsreader.models.entities.SourceEntity;

@JsonObject
public class SourceJson {
    @JsonField public String id;
    @JsonField public String name;
    @JsonField public ImageUrlJson urlsToLogos;

    @NonNull
    private Source toEntity() {
        Source s = new SourceEntity();
        s.setId(id);
        s.setName(name);;
        s.setImageUrl(urlsToLogos.getUrl());
        return s;
    }

    @NonNull
    public static List<Source> asEntities(List<SourceJson> sources) {
        List<Source> res = new ArrayList<>();
        for (SourceJson source : sources) {
            res.add(source.toEntity());
        }
        return res;
    }
}
