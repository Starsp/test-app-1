package ru.agima.testapp.word.thread;

import ru.agima.testapp.word.service.WordAnalyticProcessor;
import ru.agima.testapp.word.util.Analytic;

import java.util.Arrays;

public class CustomThreadPool {

    private final WordAnalyticWorker[] analyticWorkers;

    public CustomThreadPool(WordAnalyticProcessor processor) {
        int threadCount = Runtime.getRuntime().availableProcessors();
        analyticWorkers = new WordAnalyticWorker[threadCount];
        for (int i = 0; i < threadCount; i++) {
            analyticWorkers[i] = new WordAnalyticWorker(Analytic.getInstance(), processor);
            analyticWorkers[i].start();
        }
    }

    public void shutdownAll() {
        for (WordAnalyticWorker analyticWorker : analyticWorkers) {
            analyticWorker.shutdownSignal = true;
        }
    }

    public boolean isFinished() {
        return Arrays.stream(analyticWorkers)
                .allMatch(wordAnalyticWorker -> wordAnalyticWorker.finished);
    }

}
