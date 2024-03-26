package ru.agima.testapp.word.service;

import ru.agima.testapp.word.exception.WordAnalyticException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileAnalyzer {

    public Map<String, Integer> getFileWordCount(File file, Pattern pattern) {
        Map<String, Integer> result = new LinkedHashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String word = matcher.group(1).toUpperCase();
                    result.put(word, result.getOrDefault(word, 0) + 1);
                }
            }
            System.out.println(MessageFormat.format("File {0} is ready in thread {1}", file.getName(), Thread.currentThread().getName()));
            return result;
        } catch (IOException e) {
            throw new WordAnalyticException(e);
        }
    }


}
