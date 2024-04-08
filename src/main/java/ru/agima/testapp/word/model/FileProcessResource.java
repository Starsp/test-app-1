package ru.agima.testapp.word.model;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

public class FileProcessResource {

    private final Pattern pattern;
    private final Path sourceRootFolder;
    private final BlockingQueue<List<String>> queue;
    private final Map<String, Integer> resultMap;

    public FileProcessResource(Pattern pattern, Path path) {
        this.pattern = pattern;
        this.sourceRootFolder = path;
        this.queue = new LinkedBlockingQueue<>();
        this.resultMap = new ConcurrentHashMap<>();
    }

    public Pattern getPattern() {
        return pattern;
    }

    public BlockingQueue<List<String>> getQueue() {
        return queue;
    }

    public Map<String, Integer> getResultMap() {
        return resultMap;
    }

    public Path getSourceRootFolder() {
        return sourceRootFolder;
    }
}
