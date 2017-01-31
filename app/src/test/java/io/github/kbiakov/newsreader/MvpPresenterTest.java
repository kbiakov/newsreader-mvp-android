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

/**
 *** api(1) db(1)
 ** api(1) db(0)
 *e api(1) db(-1)
 *** api(0) db(1)
 *** api(0) db(0)
 *e api(0) db(-1)
 ** api(-1) db(1)
 *e api(-1) db(0)
 *e api(-1) db(-1)
 *
 * saving error
 */

/**
 * @param <V> View
 * @param <P> Presenter
 */
abstract class MvpPresenterTest<V extends MvpView, P extends MvpBasePresenter<V>> {
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

    /*
    @Test abstract void getFromNetwork_Success();
    @Test abstract void getFromNetwork_Empty();
    @Test abstract void getFromNetwork_NoInternet();
    @Test abstract void getFromNetwork_Fail();

    @Test abstract void getFromDb_Success();
    @Test abstract void getFromDb_Empty();
    @Test abstract void saveToDb_Success();

    @Test abstract void getFromNetworkAndDb_NoInternetAndEmptyCache();
    @Test abstract void getFromNetworkAndDb_Empty();
    */
}
