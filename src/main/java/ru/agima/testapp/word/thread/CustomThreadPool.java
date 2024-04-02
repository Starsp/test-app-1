package ru.agima.testapp.word.thread;

import ru.agima.testapp.word.exception.WordAnalyticException;
import ru.agima.testapp.word.service.WordAnalyticProcessor;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;

public class CustomThreadPool {

    private final WordAnalyticWorker[] analyticWorkers;

    public CustomThreadPool(WordAnalyticProcessor processor) {
        int threadCount = Runtime.getRuntime().availableProcessors();
        analyticWorkers = new WordAnalyticWorker[threadCount];
        for (int i = 0; i < threadCount; i++) {
            analyticWorkers[i] = new WordAnalyticWorker(processor);
            analyticWorkers[i].start();
        }
    }

    public boolean shutdownAll(Integer value, TemporalUnit timeUnit) {
        try {
            Thread.sleep(Duration.of(value, timeUnit));
        } catch (InterruptedException e) {
            throw new WordAnalyticException(e);
        } finally {
            for (WordAnalyticWorker analyticWorker : analyticWorkers) {
                analyticWorker.shutdownSignal = true;
            }
        }
        return Arrays.stream(analyticWorkers)
                .allMatch(wordAnalyticWorker -> wordAnalyticWorker.finished);
    }

}
