package io.github.kbiakov.newsreader.db;

import java.util.List;

import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.entities.Source;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public interface DbStore {
    Observable<List<Source>> getSources();
    Observable<Iterable<Source>> saveSourcesDefault(List<Source> sources);
    Disposable saveSources(List<Source> sources);

    Observable<List<Article>> getArticles(String sourceId);
    Observable<Iterable<Article>> saveArticlesDefault(List<Article> articles);
    Disposable saveArticles(List<Article> articles);
}
