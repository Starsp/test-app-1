package ru.agima.testapp.word.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class Analytic {

    private Pattern pattern;
    private Integer resultCount;
    private Map<String, Integer> resultMap;
    private static Analytic INSTANCE;

    private Analytic() {
        resultMap = new ConcurrentHashMap<>();
    }

    public static Analytic getInstance() {
        Analytic localInstance = INSTANCE;
        if (localInstance == null) {
            synchronized (Analytic.class) {
                localInstance = INSTANCE;
                if (localInstance == null) {
                    INSTANCE = localInstance = new Analytic();
                }
            }
        }
        return localInstance;
    }


    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public Map<String, Integer> getResultMap() {
        return resultMap;
    }
}
