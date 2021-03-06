package io.github.kbiakov.newsreader.screens.articles;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.SimpleSwapChangeHandler;
import com.hannesdorfmann.mosby.conductor.viewstate.lce.MvpLceViewStateController;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.Collections;
import java.util.List;

import io.github.kbiakov.newsreader.R;
import io.github.kbiakov.newsreader.datasource.DataSource;
import io.github.kbiakov.newsreader.models.entities.Article;
import io.github.kbiakov.newsreader.screens.webpage.WebpageController;

public class ArticlesController extends MvpLceViewStateController<SwipeRefreshLayout, List<Article>, ArticlesView, ArticlesPresenter>
        implements ArticlesView, SwipeRefreshLayout.OnRefreshListener {

    private ArticlesAdapter adapter;
    private String sourceId;

    public ArticlesController(String sourceId) {
        super();
        this.sourceId = sourceId;
    }

    public ArticlesController(Bundle args) {
        super(args);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_articles, container, false);

        contentView = (SwipeRefreshLayout) view.findViewById(R.id.contentView);
        contentView.setOnRefreshListener(this);

        errorView = (TextView) view.findViewById(R.id.errorView);

        adapter = new ArticlesAdapter(Collections.emptyList(),
                url -> presenter.onArticleSelected(url)
        );

        RecyclerView rvArticles = (RecyclerView) view.findViewById(R.id.rvArticles);
        rvArticles.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvArticles.setAdapter(adapter);

        return view;
    }

    @NonNull
    @Override
    public ArticlesPresenter createPresenter() {
        return new ArticlesPresenter();
    }

    // - LCE

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public void setData(List<Article> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadArticles(pullToRefresh, sourceId);
    }

    // - ArticlesView

    @Override
    public void showArticle(String articleUrl) {
        getRouter().pushController(RouterTransaction.with(new WebpageController(articleUrl))
                .pushChangeHandler(new SimpleSwapChangeHandler())
                .popChangeHandler(new SimpleSwapChangeHandler()));
    }

    // - View state

    @Override
    public List<Article> getData() {
        return adapter == null ? null : adapter.getData();
    }

    @NonNull
    @Override
    public LceViewState<List<Article>, ArticlesView> createViewState() {
        return new RetainingLceViewState<>();
    }

    // - SwipeRefreshLayout.OnRefreshListener

    @Override
    public void onRefresh() {
        loadData(true);
    }
}
