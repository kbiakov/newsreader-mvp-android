package io.github.kbiakov.newsreader.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.github.kbiakov.newsreader.BaseTest;
import io.github.kbiakov.newsreader.models.response.SourcesResponse;
import io.reactivex.subscribers.TestSubscriber;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.mockito.Mockito.mock;

public class ApiServiceTest extends BaseTest {

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
                String path = request.getPath();

                if (path.startsWith(ApiService.API_ENDPOINT + "/sources")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody(testUtils.readJson("sources"));
                } else if (path.startsWith(ApiService.API_ENDPOINT + "/articles")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody(testUtils.readJson("articles"));
                }
                return new MockResponse().setResponseCode(404);
            }
        });

        apiService = mock(ApiService.class);
    }

    @Test
    public void testGetSources() throws Exception {
        TestSubscriber<SourcesResponse> testSubscriber = new TestSubscriber<>();
        apiService.getSources(null, null, null).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);

        /*
        List<SourcesResponse> actual = testSubscriber.getOnNextEvents().get(0);

        assertEquals(7, actual.size());
        assertEquals("Android-Rate", actual.get(0).getName());
        assertEquals("andrey7mel/Android-Rate", actual.get(0).getFullName());
        assertEquals(26314692, actual.get(0).getId());
        */
    }

    @Test
    public void testGetArticles() {
        // TODO
    }

    /*
    @Test
    public void testGetSourcesIncorrect() throws Exception {
        try {
            apiInterface.getContributors("BBB", "AAA").subscribe();
            fail();
        } catch (Exception expected) {
            assertEquals("HTTP 404 OK", expected.getMessage());
        }
    }
    */

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}
