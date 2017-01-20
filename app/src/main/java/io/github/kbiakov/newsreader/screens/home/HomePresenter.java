package io.github.kbiakov.newsreader.screens.home;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import io.github.kbiakov.newsreader.App;
import io.github.kbiakov.newsreader.api.ApiService;
import io.github.kbiakov.newsreader.models.entities.Source;
import io.github.kbiakov.newsreader.models.json.SourceJson;
import io.github.kbiakov.newsreader.models.response.SourcesResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;

public class HomePresenter extends MvpBasePresenter<HomeView> {

    private static final String TAG = "Sources";

    @Inject ApiService apiService;
    @Inject ReactiveEntityStore<Persistable> dbStore;

    HomePresenter() {
        App.getAppComponent().inject(this);
    }

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

    private Observable<List<Source>> getSources() {
        return getFromNetwork().concatWith(getFromDb());
    }

    private Observable<List<Source>> getFromDb() {
        return dbStore
                .select(Source.class)
                .get()
                .observable()
                .toList()
                .toObservable()
                .doOnNext(s -> Log.e(TAG, "DB, " + s.size()));
    }

    private Observable<List<Source>> getFromNetwork() {
        return apiService
                .getSources(null, null, null)
                .onErrorReturn(t -> {
                    if (t instanceof UnknownHostException) { // no internet
                        return SourcesResponse.empty();
                    }
                    return SourcesResponse.invalid(t);
                })
                .map(SourcesResponse::getData)
                .map(SourceJson::asEntities)
                .doOnNext(this::saveSources)
                .doOnNext(s -> Log.e(TAG, "API, " + s.size()));
    }

    private Disposable saveSources(List<Source> sources) {
        return dbStore
                .upsert(sources)
                .subscribe();
    }
}
