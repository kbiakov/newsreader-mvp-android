package io.github.kbiakov.newsreader.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.kbiakov.newsreader.BaseTest;
import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.models.response.SourcesResponse;
import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.mockito.Mockito.mock;

public class ApiServiceTest extends BaseTest {

    private static final String ROUTE = ApiService.API_ENDPOINT + "/";
    private static final String SOURCES = "sources";
    private static final String ARTICLES = "articles";

    private static final String SOURCE_ID = "the-next-web";
    private static final int SOURCES_COUNT = 70;
    private static final int ARTICLES_COUNT = 10;

    private MockWebServer server;
    private ApiService apiService;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        server = new MockWebServer();
        server.start();
        server.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().startsWith(ROUTE + SOURCES)) {
                    return new MockResponse().setBody(testUtils.readJson(SOURCES));
                } else if (request.getPath().startsWith(ROUTE + ARTICLES)) {
                    return new MockResponse().setBody(testUtils.readJson(ARTICLES));
                }
                return new MockResponse().setResponseCode(404);
            }
        });

        apiService = mock(ApiService.class);
    }

    @Test
    public void testGetSources() throws Exception {
        TestObserver<SourcesResponse> testObserver =
                apiService.getSources(null, null, null).test();

        testObserver.awaitTerminalEvent();

        testObserver
                .assertNoErrors()
                .assertValue(l -> l.getData().size() == SOURCES_COUNT);
    }

    @Test
    public void testGetArticles() {
        TestObserver<ArticlesResponse> testObserver =
                apiService.getArticles(SOURCE_ID, null).test();

        testObserver.awaitTerminalEvent();

        testObserver
                .assertNoErrors()
                .assertValue(l -> l.getData().size() == ARTICLES_COUNT);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}
