package ru.agima.testapp.word.thread;

import ru.agima.testapp.word.service.FileAnalyzer;
import ru.agima.testapp.word.service.WordAnalyticProcessor;
import ru.agima.testapp.word.util.Analytic;

import java.util.List;
import java.util.Map;

public class WordAnalyticWorker extends Thread {

    volatile boolean shutdownSignal = false;
    private final Analytic config;
    private final WordAnalyticProcessor processor;
    volatile boolean finished = true;

    public WordAnalyticWorker(Analytic config, WordAnalyticProcessor processor) {
        this.config = config;
        this.processor = processor;
    }

    @Override
    public void run() {
        List<String> contentToAnalyze = null;
        while (!shutdownSignal) {
            contentToAnalyze = processor.getQueue().poll();
            if (contentToAnalyze != null) {
                System.out.println("Start read " + Thread.currentThread().getName());
                Map<String, Integer> fileWordCount = new FileAnalyzer().getFileWordCount(contentToAnalyze);
                config.getResultMap().putAll(fileWordCount);
                System.out.println("Finish read " + Thread.currentThread().getName());
            }
            if (shutdownSignal) {
                finished = false;
                break;
            }
        }
    }

}
