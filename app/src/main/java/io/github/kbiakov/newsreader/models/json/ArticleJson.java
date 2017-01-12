package io.github.kbiakov.newsreader.models.json;

import android.support.annotation.NonNull;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.entities.ArticleEntity;

@JsonObject
public class ArticleJson {
    @JsonField public String author;
    @JsonField public String title;
    @JsonField public String description;
    @JsonField public String url;
    @JsonField public String urlToImage;

    @NonNull
    private Article toEntity(String sourceId) {
        Article a = new ArticleEntity();
        a.setSourceId(sourceId);
        a.setAuthor(author);
        a.setTitle(title);
        a.setDescription(description);
        a.setUrl(url);
        a.setUrlToImage(urlToImage);
        return a;
    }

    @NonNull
    public static List<Article> asEntities(List<ArticleJson> articles, String sourceId) {
        List<Article> res = new ArrayList<>();
        for (ArticleJson article : articles) {
            res.add(article.toEntity(sourceId));
        }
        return res;
    }
}
