package io.github.kbiakov.newsreader.screens.articles;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import io.github.kbiakov.newsreader.App;
import io.github.kbiakov.newsreader.api.ApiService;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.entities.ArticleEntity;
import io.github.kbiakov.newsreader.models.json.ArticleJson;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.models.response.SortBy;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class ArticlesPresenter extends MvpBasePresenter<ArticlesView> {

    private static final String TAG = "Articles";

    @Inject ApiService apiService;
    @Inject ReactiveEntityStore<Persistable> dbStore;

    ArticlesPresenter() {
        App.getAppComponent().inject(this);
    }

    // - Interface

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

    private Observable<List<Article>> getArticles(String sourceId) {
        return getFromNetwork(sourceId).mergeWith(getFromDb(sourceId));
    }

    private Observable<List<Article>> getFromDb(String sourceId) {
        return dbStore
                .select(Article.class)
                .where(ArticleEntity.SOURCE_ID.eq(sourceId))
                .get()
                .observable()
                .toList()
                .toObservable()
                .doOnNext(s -> Log.e(TAG, "DB, " + s.size()));
    }

    private Observable<List<Article>> getFromNetwork(String sourceId) {
        return apiService
                .getArticles(sourceId, SortBy.TOP)
                .onErrorReturn(t -> {
                    if (t instanceof UnknownHostException) { // no internet
                        return ArticlesResponse.empty();
                    }
                    return ArticlesResponse.invalid(t);
                })
                .map(ArticlesResponse::getData)
                .map(a -> ArticleJson.asEntities(a, sourceId))
                .doOnNext(this::saveArticles)
                .doOnNext(s -> Log.e(TAG, "API, " + s.size()));
    }

    private Disposable saveArticles(List<Article> articles) {
        return dbStore
                .upsert(articles)
                .subscribe();
    }
}
