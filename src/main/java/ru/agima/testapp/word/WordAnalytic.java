package ru.agima.testapp.word;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordAnalytic {

    public static Map<String, Integer> getFileWordCount(File file, Pattern pattern) {
        Map<String, Integer> result = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String word = matcher.group(1).toUpperCase();
                    result.put(word, result.getOrDefault(word, 0) + 1);
                }
            }
            return result;
        } catch (IOException e) {
            throw new WordAnalyticException(e);
        }
    }


    public static void main(String[] args) throws IOException {
        ForkJoinPool customThreadPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

        Pattern pattern = Pattern.compile("(\\b[А-Яа-яA-Za-z][А-Яа-яA-Za-z\\d-]{4}\\b)");
        customThreadPool.submit(() -> {
            try (Stream<Path> walk = Files.walk(Paths.get("D:\\WORD"))) {
                List<KeyValuePair> collect = walk.parallel()
                        .filter(Files::isRegularFile)
                        .flatMap(path -> getFileWordCount(path.toFile(), pattern).entrySet().stream())
                        .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingLong(Map.Entry::getValue)))
                        .entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .limit(10)
                        .map(stringLongEntry -> new KeyValuePair(stringLongEntry.getKey(), stringLongEntry.getValue()))
                        .toList();
                System.out.println(collect);
            } catch (IOException e) {
                throw new WordAnalyticException(e);
            }
        });

    }
}