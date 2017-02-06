package io.github.kbiakov.newsreader.mock;

public interface IMock<R> {
    R mockResponse();
    R emptyResponse();
    R invalidResponse();
}
