package io.github.kbiakov.newsreader.presenters;

import org.junit.Test;

import java.util.List;

import io.github.kbiakov.newsreader.mock.IMock;
import io.github.kbiakov.newsreader.mock.SourcesMock;
import io.github.kbiakov.newsreader.models.entities.Source;
import io.github.kbiakov.newsreader.models.response.SourcesResponse;
import io.github.kbiakov.newsreader.screens.home.HomePresenter;
import io.github.kbiakov.newsreader.screens.home.HomeView;
import io.reactivex.Observable;

import static io.github.kbiakov.newsreader.mock.ArticlesMock.MOCK_SOURCE_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class SourcesPresenterTest {/*extends AbsPresenterTest<Source, SourcesResponse, HomeView, HomePresenter> {

    // - Mock

    @Override
    public IMock<SourcesResponse> createMock() {
        return new SourcesMock();
    }

    @Override
    public List<Source> createMockData() {
        return SourcesMock.createData();
    }

    // - Interface

    @Override
    public void togglePresenterForLoad() {
        presenter.loadSources(false);
    }

    @Test
    @Override
    public void testOnSomethingSelected() {
        presenter.onSourceSelected(MOCK_SOURCE_ID);

        verify(view).listArticles(MOCK_SOURCE_ID);
        verify(view, never()).showError(any(Throwable.class), false);
    }

    // - Data source

    @Override
    public Observable<SourcesResponse> makeApiRequest() {
        return api.getSources(null, null, null).toObservable();
    }

    @Override
    public Observable<List<Source>> fetchFromDb() {
        return db.getSources();
    }
    */
}
