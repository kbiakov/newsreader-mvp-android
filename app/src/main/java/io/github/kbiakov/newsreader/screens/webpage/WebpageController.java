package io.github.kbiakov.newsreader.screens.webpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby.mvp.conductor.MvpController;

import io.github.kbiakov.newsreader.R;

public class WebpageController extends MvpController<WebpageView, WebpagePresenter> implements WebpageView {

    private WebView wvPage;
    private ProgressBar pbLoading;
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

        pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);

        wvPage = (WebView) view.findViewById(R.id.wv_page);
        wvPage.getSettings().setJavaScriptEnabled(true);
        wvPage.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                presenter.onPageLoaded();
            }
        });
        wvPage.loadUrl(articleUrl);

        return view;
    }

    @NonNull
    @Override
    public WebpagePresenter createPresenter() {
        return new WebpagePresenter();
    }

    @Override
    public void loadUrl(String url) {
        pbLoading.setVisibility(View.VISIBLE);
        wvPage.loadUrl(articleUrl);
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.INVISIBLE);
    }
}
