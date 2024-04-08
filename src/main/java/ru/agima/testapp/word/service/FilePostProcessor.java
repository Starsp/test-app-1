package ru.agima.testapp.word.service;

import ru.agima.testapp.word.model.FileProcessResource;

import java.text.MessageFormat;
import java.util.Map;
import java.util.stream.Collectors;

public class FilePostProcessor {

    public void printTop(FileProcessResource resource) {
        resource.getResultMap().entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .forEach(result -> System.out.println(MessageFormat.format("{0} : {1}", result.getKey(), result.getValue())));
    }

}
