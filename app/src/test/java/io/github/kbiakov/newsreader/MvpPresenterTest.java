package io.github.kbiakov.newsreader;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.github.kbiakov.newsreader.api.ApiService;
import io.github.kbiakov.newsreader.db.DbStore;

abstract class MvpPresenterTest<V extends MvpView, P extends MvpBasePresenter<V>> {

    static final int MOCK_ITEMS_COUNT = 42;
    static final String MOCK_SOURCE_ID = "the-next-web";
    static final String MOCK_ARTICLE_URL = "http://thenextweb.com/insider/2017/01/20/director-robbed-grocery-store-piracy/";

    @Rule MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock ApiService apiService;
    @Mock DbStore dbStore;
    @Mock V view;
    @InjectMocks P presenter;

    @Before public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Before public void attachView() {
        presenter.attachView(view);
    }
    @After public void detachView() {
        presenter.detachView(true);
    }

    // - Main test cases

    @Test public abstract void getFromNetwork_Success();
    @Test public abstract void getFromNetwork_NoInternet();
    @Test public abstract void getFromNetwork_Empty();
    @Test public abstract void getFromNetwork_Fail();

    @Test public abstract void getFromDb_Success();
    @Test public abstract void getFromDb_Empty();
    @Test public abstract void saveToDb_Success();

    @Test public abstract void getFromAnywhere_Success();
    @Test public abstract void getFromAnywhere_Empty();
}
