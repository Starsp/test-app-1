package ru.agima.testapp.word.thread;

import ru.agima.testapp.word.service.FileAnalyzer;
import ru.agima.testapp.word.service.WordAnalyticProcessor;

import java.util.List;
import java.util.Map;

public class WordAnalyticWorker extends Thread {

    volatile boolean shutdownSignal = false;
    volatile boolean finished = true;
    private final WordAnalyticProcessor processor;

    public WordAnalyticWorker(WordAnalyticProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void run() {
        List<String> contentToAnalyze;
        while (!shutdownSignal) {
            contentToAnalyze = processor.getQueue().poll();
            if (contentToAnalyze != null) {
                System.out.println("Start read " + Thread.currentThread().getName());
                Map<String, Integer> fileWordCount = new FileAnalyzer().getFileWordCount(contentToAnalyze, processor.getPattern());
                processor.getResultMap().putAll(fileWordCount);
                System.out.println("Finish read " + Thread.currentThread().getName());
            }
            if (shutdownSignal) {
                finished = true;
                break;
            }
        }
    }

}
