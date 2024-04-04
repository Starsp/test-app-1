package ru.agima.testapp.word;

import ru.agima.testapp.word.model.AnalyticRequest;
import ru.agima.testapp.word.service.WordAnalyticProcessor;

import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            String msg = String.format("Expected 2 arguments but found %d", args == null ? 0 : args.length);
            throw new IllegalArgumentException(msg);
        }
        long l = System.currentTimeMillis();
        AnalyticRequest analyticRequest = new AnalyticRequest(Paths.get(args[0]), Integer.valueOf(args[1]));
        WordAnalyticProcessor service = new WordAnalyticProcessor();
        service.analyze(analyticRequest).entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .forEach(result -> System.out.println(MessageFormat.format("{0} : {1}", result.getKey(), result.getValue())));
        System.out.println(MessageFormat.format("Finished at {0} mills", System.currentTimeMillis() - l));
    }

}
