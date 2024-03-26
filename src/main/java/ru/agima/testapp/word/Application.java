package ru.agima.testapp.word;

import ru.agima.testapp.word.model.AnalyticRequest;
import ru.agima.testapp.word.service.WordAnalyticService;

import java.nio.file.Paths;
import java.text.MessageFormat;

public class Application {

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            String msg = String.format("Expected 2 arguments but found %d", args == null ? 0 : args.length);
            throw new IllegalArgumentException(msg);
        }
        long l = System.currentTimeMillis();
        AnalyticRequest analyticRequest = new AnalyticRequest(Paths.get(args[0]), Integer.valueOf(args[1]));
        WordAnalyticService wordAnalyticService = new WordAnalyticService();
        wordAnalyticService.analyze(analyticRequest).stream()
                .map(result -> MessageFormat.format("{0} : {1}", result.getKey(), result.getValue()))
                .forEach(System.out::println);
        System.out.println(MessageFormat.format("Finished at {0} mills", System.currentTimeMillis() - l));
    }

}
