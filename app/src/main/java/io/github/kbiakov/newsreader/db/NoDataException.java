package io.github.kbiakov.newsreader.db;

public class NoDataException extends RuntimeException {
    public NoDataException() {
        super("No data available to show.");
    }

    public static NoDataException create() {
        return new NoDataException();
    }
}
