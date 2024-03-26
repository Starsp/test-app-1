package ru.agima.testapp.word.service;

import ru.agima.testapp.word.exception.WordAnalyticException;
import ru.agima.testapp.word.model.AnalyticRequest;
import ru.agima.testapp.word.model.AnalyticResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordAnalyticService {

    private final FileAnalyzer fileAnalyzer = new FileAnalyzer();

    public List<AnalyticResult> analyze(AnalyticRequest analyticRequest) {
        if (Files.notExists(analyticRequest.getTarget())) {
            throw new IllegalArgumentException(String.format("File not found %s", analyticRequest.getTarget().toAbsolutePath()));
        }
        System.out.println("STARTED..");
        Pattern pattern = Pattern.compile(String.format("(\\b[А-Яа-яA-Za-z]*[А-Яа-яA-Za-z\\d-]{%d}\\b)", analyticRequest.getMinWordLength()));
        try (Stream<Path> walk = Files.walk(analyticRequest.getTarget())) {
            return walk.toList()
                    .parallelStream()
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toFile().getName().toLowerCase().endsWith(".txt"))
                    .flatMap(path -> fileAnalyzer.getFileWordCount(path.toFile(), pattern).entrySet().stream())
                    .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(10)
                    .map(stringLongEntry -> new AnalyticResult(stringLongEntry.getKey(), stringLongEntry.getValue()))
                    .toList();
        } catch (IOException e) {
            throw new WordAnalyticException(e);
        }

    }

}
