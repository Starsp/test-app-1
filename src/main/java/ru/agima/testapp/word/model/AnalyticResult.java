package ru.agima.testapp.word.model;

public class AnalyticResult {

    private String key;
    private Long value;

    public AnalyticResult(String key, Long value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Long getValue() {
        return value;
    }
}
