package io.github.kbiakov.newsreader;

import java.util.List;

import io.reactivex.Observable;

interface MockProvider<M, R> {
    Observable<R> getFromNetwork();
    Observable<R> noInternetError();
    Observable<List<M>> getFromDb();
    Observable<Iterable<M>> saveToDb();
    Observable<List<M>> getFromAnywhere();

    R mockResponse();
    R emptyResponse();
    R invalidResponse();

    List<M> mockData();
    List<M> emptyData();
}
