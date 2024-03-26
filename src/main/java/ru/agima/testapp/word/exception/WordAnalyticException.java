package ru.agima.testapp.word.exception;

public class WordAnalyticException extends RuntimeException {

    public WordAnalyticException(Exception e) {
        super(e);
    }
}
