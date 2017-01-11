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

    void loadSources(final boolean pullToRefresh) {
        if (isViewAttached()) {
            getView().showLoading(pullToRefresh);
        }

        getFromNetwork()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext(s -> {
                    Log.e("!!!", s.size() + "");

                    for (Source source : s) {
                        if (source == null) {
                            Log.e("***", "Source is null! =(");
                            continue;
                        }
                        try {
                            Log.e("***", source.name);
                        } catch (NullPointerException e) {
                            Log.e("***", "Name not readed");
                        }
                    }

                    Log.e("***", "1");

                    getFromDb().subscribe(asds -> {
                        Log.e("!!!", "Saved: " + asds.size());
                    });

                    Log.e("***", "2");
                })
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

    private Observable<List<Source>> getFromDb() {
        return Observable.just(DataSource.db()
                .select(Source.class)
                .get()
                .toList());
    }

    private Observable<List<Source>> getFromNetwork() {
        return DataSource.api()
                .getSources(null, null, null)
                .map(SourcesResponse::getData)
                .map(this::toEntity)
                .doOnNext(DataSource::saveSources);
    }

    private Single<List<Source>> getSources() {
        return Observable.concat(getFromDb(), getFromNetwork())
                .first(DataSource.emptySources());
    }

    private List<Source> toEntity(List<SourceJson> sourceJsons) {
        List<Source> res = new ArrayList<>();
        for (SourceJson json : sourceJsons) {
            res.add(json.toEntity());
        }
        return res;
    }
}
