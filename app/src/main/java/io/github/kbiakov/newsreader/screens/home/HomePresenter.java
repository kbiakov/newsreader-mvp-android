package io.github.kbiakov.newsreader.screens.home;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.github.kbiakov.newsreader.datasource.DataSource;
import io.github.kbiakov.newsreader.models.entities.Source;
import io.github.kbiakov.newsreader.models.json.SourceJson;
import io.github.kbiakov.newsreader.models.response.SourcesResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class HomePresenter extends MvpBasePresenter<HomeView> {

    // - Interface

    void loadSources(final boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showLoading(pullToRefresh);
        }
        getSources()
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

    void onSourceSelected(String sourceId) {
        if (isViewAttached()) {
            getView().listArticles(sourceId);
        }
    }

    // - Data source

    private Single<List<Source>> getSources() {
        return Observable.concat(getFromDb(), getFromNetwork())
                .first(DataSource.emptySources());
    }

    private Observable<List<Source>> getFromDb() {
        return DataSource.db()
                .select(Source.class)
                .get()
                .observable()
                .toList()
                .toObservable();
    }

    private Observable<List<Source>> getFromNetwork() {
        return DataSource.api()
                .getSources(null, null, null)
                .map(SourcesResponse::getData)
                .map(SourceJson::asEntities)
                .doOnNext(DataSource::saveSources);
    }
}
