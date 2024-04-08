package ru.agima.testapp.word.model;


import java.nio.file.Path;

public class ProcessFileRequest {

    private Path target;
    private Integer minWordLength;


    public ProcessFileRequest(Path target, Integer minWordLength) {
        this.target = target;
        if (minWordLength == null || minWordLength < 1) {
            throw new IllegalArgumentException("Minimum word length must be greater than zero");
        }
        this.minWordLength = minWordLength;
    }

    public Path getTarget() {
        return target;
    }

    public Integer getMinWordLength() {
        return minWordLength;
    }

}
