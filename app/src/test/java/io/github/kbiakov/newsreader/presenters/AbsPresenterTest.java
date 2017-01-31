package io.github.kbiakov.newsreader.presenters;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

import io.github.kbiakov.newsreader.api.ApiService;
import io.github.kbiakov.newsreader.api.mocks.IMock;
import io.github.kbiakov.newsreader.db.DbStore;
import io.github.kbiakov.newsreader.db.NoDataException;
import io.github.kbiakov.newsreader.models.response.Response;
import io.reactivex.Observable;

import static io.reactivex.Observable.error;
import static io.reactivex.Observable.just;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

abstract class AbsPresenterTest<M, R extends Response<?>, V extends MvpLceView<List<M>>, P extends MvpBasePresenter<V>> {

    @Rule MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock ApiService api;
    @Mock DbStore db;
    @Mock V view;
    @InjectMocks P presenter;

    private final IMock<R> mock = createMock();

    abstract IMock<R> createMock();
    abstract List<M> createMockData();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void attachView() {
        presenter.attachView(view);
    }

    @After
    public void detachView() {
        presenter.detachView(true);
    }

    // - Interface

    @Test
    void testLoadSomething() {
        when(makeApiRequest()).thenReturn(
                just(mock.mockResponse())
        );
        when(fetchFromDb()).thenReturn(
                just(createMockData())
        );

        togglePresenterForLoad();

        verify(view).showLoading(false);
        verify(view).setData(createMockData());
        verify(view, never()).showError(any(Throwable.class), false);
    }

    abstract void togglePresenterForLoad();

    @Test
    abstract void testOnSomethingSelected();

    // - Test cases

    @Test
    void testLoad_NoInternet() {
        when(makeApiRequest()).thenReturn(
                error(new UnknownHostException())
        );
        when(fetchFromDb()).thenReturn(
                just(createMockData())
        );

        togglePresenterForLoad();

        verify(view).showLoading(false);
        verify(view).setData(createMockData());
        verify(view, never()).showError(any(Throwable.class), false);
    }

    @Test
    void testLoadSources_NoData() {
        when(makeApiRequest()).thenReturn(
                error(new UnknownHostException())
        );
        when(fetchFromDb()).thenReturn(
                just(Collections.emptyList())
        );

        togglePresenterForLoad();

        verify(view).showLoading(false);
        verify(view, never()).setData(any());
        verify(view).showError(NoDataException.create(), false);
    }

    @Test
    void testLoadArticles_InternetError() {
        when(makeApiRequest()).thenReturn(
                error(new RuntimeException("Something goes wrong."))
        );
        when(fetchFromDb()).thenReturn(
                just(createMockData())
        );

        togglePresenterForLoad();

        verify(view).showLoading(false);
        verify(view, never()).setData(createMockData());
        verify(view).showError(any(Throwable.class), false);
    }

    @Test
    void testLoadArticles_DbError() {
        when(makeApiRequest()).thenReturn(
                just(mock.mockResponse())
        );
        when(fetchFromDb()).thenReturn(
                error(new RuntimeException("Something goes wrong."))
        );

        togglePresenterForLoad();

        verify(view).showLoading(false);
        verify(view, never()).setData(createMockData());
        verify(view).showError(any(Throwable.class), false);
    }

    // - Data source

    abstract Observable<R> makeApiRequest();

    abstract Observable<List<M>> fetchFromDb();
}
