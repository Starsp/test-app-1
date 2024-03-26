package ru.agima.testapp.word.model;


import java.nio.file.Path;

public class AnalyticRequest {

    private Path target;
    private Integer minWordLength;
    private Integer count;


    public AnalyticRequest(Path target, Integer minWordLength) {
        this.target = target;
        if (minWordLength == null || minWordLength < 1) {
            throw new IllegalArgumentException("Minimum word length must be greater than zero");
        }
        this.minWordLength = minWordLength;
    }

    public Path getTarget() {
        return target;
    }

    public void setTarget(Path target) {
        this.target = target;
    }

    public Integer getMinWordLength() {
        return minWordLength;
    }

    public void setMinWordLength(Integer minWordLength) {
        this.minWordLength = minWordLength;
    }
}
