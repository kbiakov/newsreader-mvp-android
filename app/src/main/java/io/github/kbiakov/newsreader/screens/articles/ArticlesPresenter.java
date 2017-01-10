package io.github.kbiakov.newsreader.screens.articles;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.github.kbiakov.newsreader.datasource.DataSource;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.models.json.ArticleJson;
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

    private Single<List<Article>> getArticles(String sourceId) {
        Observable<List<Article>> db = Observable.defer(() -> Observable.just(DataSource.db()
                .select(Article.class)
                .where(Article.SOURCE_ID.eq(sourceId))
                .get()
                .toList()));

        Observable<List<Article>> api = DataSource.api()
                .getArticles(sourceId, SortBy.TOP)
                .map(ArticlesResponse::getData)
                .map(a -> toEntity(a, sourceId))
                .doOnNext(DataSource::saveArticles);

        return Observable.concat(db, api)
                .first(DataSource.emptyArticles());
    }

    private List<Article> toEntity(List<ArticleJson> articles, String sourceId) {
        List<Article> res = new ArrayList<>();
        for (ArticleJson json: articles) {
            Article a = (Article) json;
            a.setSourceId(sourceId);
            res.add(a);
        }
        return res;
    }
}
