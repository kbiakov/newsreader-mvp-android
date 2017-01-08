package io.github.kbiakov.newsreader.screens.articles;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import io.github.kbiakov.newsreader.datasource.DataSource;
import io.github.kbiakov.newsreader.models.Article;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.models.response.SortBy;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class ArticlesPresenter extends MvpBasePresenter<ArticlesView> {

    void loadArticles(boolean pullToRefresh, String sourceId) {
        if (isViewAttached()) {
            getView().showLoading(pullToRefresh);
        }

        getArticlesFromNetwork(sourceId)
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

    private Observable<List<Article>> getArticlesFromNetwork(String sourceId) {
        return DataSource.api()
                .getArticles(sourceId, SortBy.TOP)
                .map(ArticlesResponse::getData)
                .doOnNext(this::saveArticles);
    }

    /*
    private Single<List<Article>> getArticles(String sourceId) {
        Observable<List<Article>> db = Observable.defer(() -> Observable.just(DataSource.db()
                .select(Article.class)
                //.where(ArticleEntity.SOURCE_ID.equals(sourceId)) // TODO
                .get()
                .toList()));

        Observable<List<Article>> api = getArticlesFromNetwork(sourceId);

        return Observable.concat(db, api)
                .first(DataSource.emptyArticles());
    }
    */

    private Observable<Iterable<Article>> saveArticles(Iterable<Article> articles) {
        return DataSource.db()
                .insert(articles)
                .toObservable();
    }
}