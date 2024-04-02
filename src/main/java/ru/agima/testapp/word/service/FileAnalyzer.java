package ru.agima.testapp.word.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileAnalyzer {

    public Map<String, Integer> getFileWordCount(List<String> content, Pattern pattern) {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (String line : content) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String word = matcher.group(1).toUpperCase();
                result.put(word, result.getOrDefault(word, 0) + 1);
            }
        }
        return result;
    }


}
