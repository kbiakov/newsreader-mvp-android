package io.github.kbiakov.newsreader.api.mocks;

public interface IMock<R> {
    R mockResponse();
    R emptyResponse();
    R invalidResponse();
}
