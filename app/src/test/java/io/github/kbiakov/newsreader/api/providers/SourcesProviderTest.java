package io.github.kbiakov.newsreader.api.providers;

import io.github.kbiakov.newsreader.api.mocks.IMock;
import io.github.kbiakov.newsreader.api.mocks.SourcesMock;
import io.github.kbiakov.newsreader.models.response.SourcesResponse;
import io.reactivex.Single;

public class SourcesProviderTest extends ProviderTest<SourcesResponse> {
    @Override
    IMock<SourcesResponse> createMock() {
        return new SourcesMock();
    }

    @Override
    Single<SourcesResponse> apiRequest() {
        return apiService.getSources(null, null, null);
    }
}
