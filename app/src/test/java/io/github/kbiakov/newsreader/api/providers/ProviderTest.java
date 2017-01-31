package io.github.kbiakov.newsreader.api.providers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.net.UnknownHostException;

import io.github.kbiakov.newsreader.api.ApiService;
import io.github.kbiakov.newsreader.api.mocks.IMock;
import io.github.kbiakov.newsreader.models.response.Response;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static io.reactivex.Observable.error;
import static io.reactivex.Observable.just;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public abstract class ProviderTest<R extends Response<?>> {

    @Rule MockitoRule rule = MockitoJUnit.rule();

    @Mock ApiService apiService;

    @InjectMocks SourcesProvider provider;

    private final IMock<R> mock = createMock();

    abstract IMock<R> createMock();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRequestSuccess() {
        when(makeApiRequest()).thenReturn(
                just(mock.mockResponse())
        );

        TestObserver<R> testObserver = makeApiRequest().test();
        testObserver.awaitTerminalEvent();
        testObserver
                .assertNoErrors()
                .assertValue(l -> l.getData().size() == mock.mockResponse().getData().size());
    }

    @Test
    public void testResponseEmpty() {
        when(makeApiRequest()).thenReturn(
                just(mock.emptyResponse())
        );

        TestObserver<R> testObserver = makeApiRequest().test();
        testObserver.awaitTerminalEvent();
        testObserver
                .assertNoErrors()
                .assertValue(l -> l.getData().isEmpty());
    }

    @Test
    public void testNoInternet() {
        when(makeApiRequest()).thenReturn(
                error(new UnknownHostException())
        );

        TestObserver<R> testObserver = makeApiRequest().test();
        testObserver.awaitTerminalEvent();
        testObserver.assertError(UnknownHostException.class);
    }

    @Test
    public void testInvalidResponse() {
        when(makeApiRequest()).thenReturn(
                just(mock.invalidResponse())
        );

        TestObserver<R> testObserver = makeApiRequest().test();
        testObserver.awaitTerminalEvent();
        testObserver.assertError(any(Throwable.class));
    }

    abstract Single<R> apiRequest();

    private Observable<R> makeApiRequest() {
        return apiRequest().toObservable();
    }
}
