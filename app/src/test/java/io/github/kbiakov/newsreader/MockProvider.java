package io.github.kbiakov.newsreader;

import java.util.List;

import io.reactivex.Observable;

interface MockProvider<M, R> {
    Observable<R> getFromNetwork();
    Observable<List<M>> getFromDb();

    List<M> mockData();
    List<M> emptyData();

    R mockResponse();
    R emptyResponse();
    R invalidResponse();
}
