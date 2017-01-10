package io.github.kbiakov.newsreader.screens.webpage;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface WebpageView extends MvpView {
    void loadUrl(String url);
    void hideLoading();
}
