package io.github.kbiakov.newsreader.screens.webpage;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class WebpagePresenter extends MvpBasePresenter<WebpageView> {

    void loadPage(String url) {
        if (isViewAttached()) {
            getView().loadUrl(url);
        }
    }

    void onPageLoaded() {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }
}
