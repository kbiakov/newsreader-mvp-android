package io.github.kbiakov.newsreader.db;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static io.reactivex.Observable.just;
import static org.mockito.Mockito.when;

abstract class DbTest<M> {

    @Rule MockitoRule rule = MockitoJUnit.rule();

    @Mock DbStore dbStore;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFetchSuccess() {
        when(fetch()).thenReturn(
                just(mockData())
        );

        TestObserver<List<M>> testObserver = fetch().test();
        testObserver.awaitTerminalEvent();
        testObserver
                .assertNoErrors()
                .assertValue(l -> l.size() == mockData().size());
    }

    abstract List<M> mockData();

    @Test
    public void testFetchEmpty() {
        when(fetch()).thenReturn(
                just(Collections.emptyList())
        );

        TestObserver<List<M>> testObserver = fetch().test();
        testObserver.awaitTerminalEvent();
        testObserver
                .assertNoErrors()
                .assertValue(List::isEmpty);
    }

    abstract Observable<List<M>> fetch();
}
