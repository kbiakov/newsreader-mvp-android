package io.github.kbiakov.newsreader.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import io.github.kbiakov.newsreader.models.response.ArticlesResponse;
import io.github.kbiakov.newsreader.models.response.SourcesResponse;
import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.mockito.Mockito.mock;

public class ApiServiceTest {

    private static final String ROUTE = ApiService.API_ENDPOINT + "/";
    private static final String SOURCES = "sources";
    private static final String ARTICLES = "articles";

    private static final String SOURCE_ID = "the-next-web";
    private static final int SOURCES_COUNT = 70;
    private static final int ARTICLES_COUNT = 10;

    private MockWebServer server;
    private ApiService apiService;

    private final Utils u = new Utils();

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        server.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().startsWith(ROUTE + SOURCES)) {
                    return u.json(SOURCES);
                } else if (request.getPath().startsWith(ROUTE + ARTICLES)) {
                    return u.json(ARTICLES);
                }
                return u.notFound();
            }
        });

        apiService = mock(ApiService.class);
    }

    @Test
    public void testGetSources() throws Exception {
        TestObserver<SourcesResponse> testObserver = apiService.getSources(null, null, null).test();
        testObserver.awaitTerminalEvent();
        testObserver
                .assertNoErrors()
                .assertValue(l -> l.getData().size() == SOURCES_COUNT);
    }

    @Test
    public void testGetArticles() {
        TestObserver<ArticlesResponse> testObserver = apiService.getArticles(SOURCE_ID, null).test();
        testObserver.awaitTerminalEvent();
        testObserver
                .assertNoErrors()
                .assertValue(l -> l.getData().size() == ARTICLES_COUNT);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    private static class Utils {
        MockResponse json(String target) {
            return new MockResponse().setBody(readJson(target));
        }

        MockResponse notFound() {
            return new MockResponse().setResponseCode(404);
        }

        private String readJson(String name) {
            return readString("json/" + name + ".json");
        }

        private String readString(String fileName) {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
            try {
                int size = stream.available();
                byte[] buffer = new byte[size];
                int result = stream.read(buffer);
                return new String(buffer, "utf8");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
