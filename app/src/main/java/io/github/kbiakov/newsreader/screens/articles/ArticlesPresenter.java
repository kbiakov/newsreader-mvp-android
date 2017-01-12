package io.github.kbiakov.newsreader.screens.articles;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import io.github.kbiakov.newsreader.datasource.DataSource;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.entities.ArticleEntity;
import io.github.kbiakov.newsreader.models.json.ArticleJson;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.models.response.SortBy;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class ArticlesPresenter extends MvpBasePresenter<ArticlesView> {

    // - interface

    void loadArticles(boolean pullToRefresh, String sourceId) {
        if (isViewAttached()) {
            getView().showLoading(pullToRefresh);
        }

        getArticles(sourceId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    if (isViewAttached()) {
                        getView().setData(s);
                        getView().showContent();
                    }
                }, e -> {
                    if (isViewAttached()) {
                        getView().showError(e, pullToRefresh);
                    }
                });
    }

    void onArticleSelected(String articleUrl) {
        if (isViewAttached()) {
            getView().showArticle(articleUrl);
        }
    }

    // - Data source

    private Single<List<Article>> getArticles(String sourceId) {
        return Observable.concat(getFromDb(sourceId), getFromNetwork(sourceId))
                .first(DataSource.emptyArticles());
    }

    private Observable<List<Article>> getFromDb(String sourceId) {
        return DataSource.db()
                .select(Article.class)
                .where(ArticleEntity.SOURCE_ID.eq(sourceId))
                .get()
                .observable()
                .toList()
                .toObservable();
    }

    private Observable<List<Article>> getFromNetwork(String sourceId) {
        return DataSource.api()
                .getArticles(sourceId, SortBy.TOP)
                .map(ArticlesResponse::getData)
                .map(a -> ArticleJson.asEntities(a, sourceId))
                .doOnNext(DataSource::saveArticles);
    }
}
