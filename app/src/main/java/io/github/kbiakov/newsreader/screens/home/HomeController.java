package io.github.kbiakov.newsreader.screens.home;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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

import java.util.List;

import butterknife.ButterKnife;
import io.github.kbiakov.newsreader.datasource.DataSource;
import io.github.kbiakov.newsreader.screens.articles.ArticlesController;
import io.github.kbiakov.newsreader.R;
import io.github.kbiakov.newsreader.models.entities.Source;

public class HomeController extends MvpLceViewStateController<SwipeRefreshLayout, List<Source>, HomeView, HomePresenter>
        implements HomeView, SwipeRefreshLayout.OnRefreshListener {

    private SourcesAdapter adapter;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_home, container, false);

        contentView = (SwipeRefreshLayout) view.findViewById(R.id.contentView);
        contentView.setOnRefreshListener(this);

        errorView = (TextView) view.findViewById(R.id.errorView);

        adapter = new SourcesAdapter(DataSource.emptySources(),
                id -> presenter.onSourceSelected(id)
        );

        RecyclerView rvSources = (RecyclerView) view.findViewById(R.id.rvSources);
        rvSources.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvSources.setHasFixedSize(true);
        rvSources.setAdapter(adapter);

        return view;
    }

    @NonNull
    @Override
    public HomePresenter createPresenter() {
        return new HomePresenter();
    }

    // - LCE

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public void setData(List<Source> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadSources(pullToRefresh);
    }

    // - HomeView

    @Override
    public void listArticles(String sourceId) {
        getRouter().pushController(RouterTransaction.with(new ArticlesController(sourceId))
                .pushChangeHandler(new SimpleSwapChangeHandler())
                .popChangeHandler(new SimpleSwapChangeHandler()));
    }

    // - View state

    @Override
    public List<Source> getData() {
        return adapter == null ? null : adapter.getData();
    }

    @NonNull
    @Override
    public LceViewState<List<Source>, HomeView> createViewState() {
        return new RetainingLceViewState<>();
    }

    // - SwipeRefreshLayout.OnRefreshListener

    @Override
    public void onRefresh() {
        loadData(true);
    }
}
