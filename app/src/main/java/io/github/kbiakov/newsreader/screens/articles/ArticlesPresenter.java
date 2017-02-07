package io.github.kbiakov.newsreader.screens.articles;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.github.kbiakov.newsreader.App;
import io.github.kbiakov.newsreader.api.providers.ArticlesProvider;
import io.github.kbiakov.newsreader.db.DbStore;
import io.github.kbiakov.newsreader.db.NoDataException;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticlesPresenter extends MvpBasePresenter<ArticlesView> {

    private static final String TAG = "Articles";

    @Inject ArticlesProvider api;
    @Inject DbStore db;

    @Inject
    ArticlesPresenter(ArticlesProvider api, DbStore db) {
        this.api = api;
        this.db = db;
    }

    ArticlesPresenter() {
        App.getAppComponent().inject(this);
    }

    // - Interface

    public void loadArticles(boolean pullToRefresh, String sourceId) {
        if (isViewAttached()) {
            getView().showLoading(pullToRefresh);
        }

        getArticles(sourceId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    if (s.isEmpty()) {
                        getView().showError(NoDataException.create(), pullToRefresh);
                    } else {
                        getView().setData(s);
                        getView().showContent();
                    }
                }, e -> {
                    if (isViewAttached()) {
                        getView().showError(e, pullToRefresh);
                    }
                });
    }

    public void onArticleSelected(String articleUrl) {
        if (isViewAttached()) {
            getView().showArticle(articleUrl);
        }
    }

    // - Data source

    private Observable<List<Article>> getArticles(String sourceId) {
        return getFromNetwork(sourceId).concatWith(getFromDb(sourceId));
    }

    private Observable<List<Article>> getFromNetwork(String sourceId) {
        return api
                .getArticles(sourceId)
                .doOnNext(this::saveToDb)
                .doOnNext(s -> Log.i(TAG, "API, " + s.size()));
    }

    private Observable<List<Article>> getFromDb(String sourceId) {
        return db
                .getArticles(sourceId)
                .doOnNext(s -> Log.i(TAG, "DB, " + s.size()));
    }

    private Disposable saveToDb(List<Article> articles) {
        return db.saveArticles(articles);
    }
}
