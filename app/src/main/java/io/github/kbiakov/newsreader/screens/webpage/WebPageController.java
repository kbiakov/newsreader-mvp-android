package io.github.kbiakov.newsreader.screens.webpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.hannesdorfmann.mosby.mvp.conductor.MvpController;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kbiakov.newsreader.R;

public class WebpageController extends MvpController<WebpageView, WebpagePresenter> {

    @BindView(R.id.wv_page)
    private WebView wvPage;
    private String articleUrl;

    public WebpageController(String articleUrl) {
        super();
        this.articleUrl = articleUrl;
    }

    public WebpageController(Bundle args) {
        super(args);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_webpage, container, false);
        ButterKnife.bind(this, view);
        wvPage.loadUrl(articleUrl);
        return view;
    }

    @NonNull
    @Override
    public WebpagePresenter createPresenter() {
        return new WebpagePresenter();
    }
}
