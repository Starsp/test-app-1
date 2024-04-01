package ru.agima.testapp.word.service;

import ru.agima.testapp.word.exception.WordAnalyticException;
import ru.agima.testapp.word.model.AnalyticRequest;
import ru.agima.testapp.word.model.AnalyticResult;
import ru.agima.testapp.word.thread.CustomThreadPool;
import ru.agima.testapp.word.util.Analytic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordAnalyticProcessor {

    private final BlockingQueue<List<String>> queue;
    private final int processLimit;

    public WordAnalyticProcessor() {
        this.queue = new LinkedBlockingQueue<>();
        this.processLimit = Runtime.getRuntime().availableProcessors() * 5;
    }

    public List<AnalyticResult> analyze(AnalyticRequest request) {
        if (Files.notExists(request.getTarget())) {
            throw new IllegalArgumentException(String.format("File not found %s", request.getTarget().toAbsolutePath()));
        }
        System.out.println("STARTED..");
        Pattern pattern = Pattern.compile(String.format("(\\b[А-Яа-яA-Za-z]*[А-Яа-яA-Za-z\\d-]{%d}\\b)", request.getMinWordLength()));
        Analytic config = Analytic.getInstance();
        config.setPattern(pattern);
        CustomThreadPool customThreadPool = new CustomThreadPool(this);
        try (Stream<Path> walk = Files.walk(request.getTarget())) {
            List<Path> collect = walk
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toFile().getName().toLowerCase().endsWith(".txt"))
                    .toList();
            int bufferSize = 0;
            List<String> buffer = new ArrayList<>();
            for (Path path : collect) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(path)))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        buffer.add(line);
                        while (queue.size() >= processLimit) {
                        }
                        bufferSize += line.length();
                        if (bufferSize >= 100000) {
                            queue.add(buffer);
                            bufferSize = 0;
                            buffer = new ArrayList<>();
                        }
                    }
                }
                System.out.println(MessageFormat.format("File {0} is ready in thread {1}", path.toFile().getName(), Thread.currentThread().getName()));
            }
            queue.add(buffer);
        } catch (IOException e) {
            throw new WordAnalyticException(e);
        }
        while (!customThreadPool.isFinished()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        customThreadPool.shutdownAll();
        return config.getResultMap().entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(stringLongEntry -> new AnalyticResult(stringLongEntry.getKey(), stringLongEntry.getValue()))
                .toList();
    }


    public BlockingQueue<List<String>> getQueue() {
        return queue;
    }
}