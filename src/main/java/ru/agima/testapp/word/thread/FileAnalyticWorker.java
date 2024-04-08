package ru.agima.testapp.word.thread;

import ru.agima.testapp.word.model.FileProcessResource;
import ru.agima.testapp.word.service.FileAnalyzer;

import java.util.List;
import java.util.Map;

public class FileAnalyticWorker extends Thread {

    volatile boolean shutdownSignal = false;
    volatile boolean finished = true;
    private final FileProcessResource resource;

    public FileAnalyticWorker(FileProcessResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        List<String> contentToAnalyze;
        while (!shutdownSignal) {
            contentToAnalyze = resource.getQueue().poll();
            if (contentToAnalyze != null) {
                System.out.println("Start read " + Thread.currentThread().getName());
                Map<String, Integer> fileWordCount = new FileAnalyzer().getFileWordCount(contentToAnalyze, resource.getPattern());
                resource.getResultMap().putAll(fileWordCount);
                System.out.println("Finish read " + Thread.currentThread().getName());
            }
            if (shutdownSignal) {
                finished = true;
                break;
            }
        }
    }

}
