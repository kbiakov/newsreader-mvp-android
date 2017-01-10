package io.github.kbiakov.newsreader.screens.home;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import io.github.kbiakov.newsreader.datasource.DataSource;
import io.github.kbiakov.newsreader.models.entities.Source;
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

    private Single<List<Source>> getSources() {
        Observable<List<Source>> db = Observable.defer(() -> Observable.just(DataSource.db()
                .select(Source.class)
                .get()
                .toList()));

        Observable<List<Source>> api = DataSource.api()
                .getSources(null, null, null)
                .map(SourcesResponse::getData)
                .doOnNext(this::saveSources);

        return Observable.concat(db, api)
                .first(DataSource.emptySources());
    }

    private Observable<Iterable<Source>> saveSources(List<Source> sources) {
        return DataSource.db()
                .insert(sources)
                .toObservable();
    }
}
