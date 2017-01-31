package io.github.kbiakov.newsreader.api.mocks;

import java.util.ArrayList;
import java.util.List;

import io.github.kbiakov.newsreader.models.entities.Source;
import io.github.kbiakov.newsreader.models.entities.SourceEntity;
import io.github.kbiakov.newsreader.models.json.ArticleJson;
import io.github.kbiakov.newsreader.models.json.SourceJson;
import io.github.kbiakov.newsreader.models.response.SourcesResponse;

public class SourcesMock implements IMock<SourcesResponse> {

    public static final int MOCK_SOURCES_COUNT = 42;

    @Override
    public SourcesResponse mockResponse() {
        SourcesResponse res = new SourcesResponse();
        res.setData(createJsons());
        return res;
    }

    private static List<SourceJson> createJsons() {
        List<SourceJson> sourceJsons = new ArrayList<>();
        for (int i = 0; i < MOCK_SOURCES_COUNT; i++) {
            sourceJsons.add(new SourceJson());
        }
        return sourceJsons;
    }

    public static List<Source> create() {
        List<Source> sources = new ArrayList<>();
        for (int i = 0; i < MOCK_SOURCES_COUNT; i++) {
            sources.add(new SourceEntity());
        }
        return sources;
    }

    @Override
    public SourcesResponse emptyResponse() {
        return SourcesResponse.empty();
    }

    @Override
    public SourcesResponse invalidResponse() {
        return SourcesResponse.invalid(new RuntimeException("Mocked invalid response."));
    }
}
