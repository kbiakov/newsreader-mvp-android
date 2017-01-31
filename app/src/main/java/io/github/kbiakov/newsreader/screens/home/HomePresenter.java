package io.github.kbiakov.newsreader.screens.home;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.github.kbiakov.newsreader.App;
import io.github.kbiakov.newsreader.api.providers.SourcesProvider;
import io.github.kbiakov.newsreader.db.DbStore;
import io.github.kbiakov.newsreader.db.NoDataException;
import io.github.kbiakov.newsreader.models.entities.Source;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter extends MvpBasePresenter<HomeView> {

    private static final String TAG = "Sources";

    @Inject SourcesProvider api;
    @Inject DbStore db;

    HomePresenter() {
        App.getAppComponent().inject(this);
    }

    // - Interface

    public void loadSources(final boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showLoading(pullToRefresh);
        }

        getSources()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    if (isViewAttached()) {
                        if (s.isEmpty()) {
                            getView().showError(NoDataException.create(), pullToRefresh);
                        } else {
                            getView().setData(s);
                            getView().showContent();
                        }
                    }
                }, e -> {
                    if (isViewAttached()) {
                        getView().showError(e, pullToRefresh);
                    }
                });
    }

    public void onSourceSelected(String sourceId) {
        if (isViewAttached()) {
            getView().listArticles(sourceId);
        }
    }

    // - Data source

    private Observable<List<Source>> getSources() {
        return getFromNetwork().concatWith(getFromDb());
    }

    private Observable<List<Source>> getFromNetwork() {
        return api
                .getSources()
                .doOnNext(this::saveSources)
                .doOnNext(s -> Log.i(TAG, "API, " + s.size()));
    }

    private Observable<List<Source>> getFromDb() {
        return db
                .getSources()
                .doOnNext(s -> Log.i(TAG, "DB, " + s.size()));
    }

    private Disposable saveSources(List<Source> sources) {
        return db.saveSources(sources);
    }
}
