package ru.agima.testapp.word.thread;

import ru.agima.testapp.word.exception.WordAnalyticException;
import ru.agima.testapp.word.model.FileProcessResource;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;

public class CustomThreadPool {

    private final FileAnalyticWorker[] analyticWorkers;

    public CustomThreadPool(FileProcessResource resource) {
        int threadCount = Runtime.getRuntime().availableProcessors();
        analyticWorkers = new FileAnalyticWorker[threadCount];
        for (int i = 0; i < threadCount; i++) {
            analyticWorkers[i] = new FileAnalyticWorker(resource);
            analyticWorkers[i].start();
        }
    }

    public void awaitTermination(Integer value, TemporalUnit timeUnit) {
        try {
            Thread.sleep(Duration.of(value, timeUnit));
        } catch (InterruptedException e) {
            throw new WordAnalyticException(e);
        } finally {
            for (FileAnalyticWorker analyticWorker : analyticWorkers) {
                analyticWorker.shutdownSignal = true;
            }
        }
        Arrays.stream(analyticWorkers)
                .filter(fileAnalyticWorker -> !fileAnalyticWorker.finished)
                .forEach(Thread::interrupt);
    }

}
