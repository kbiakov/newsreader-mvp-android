package io.github.kbiakov.newsreader.db;

import java.util.List;

import io.github.kbiakov.newsreader.api.mocks.SourcesMock;
import io.github.kbiakov.newsreader.models.entities.Source;
import io.reactivex.Observable;

public class SourcesDbTest extends AbsDbTest<Source> {
    @Override
    List<Source> mockData() {
        return SourcesMock.createData();
    }

    @Override
    Observable<List<Source>> fetch() {
        return dbStore.getSources();
    }
}
