package ru.agima.testapp.word.service;

import ru.agima.testapp.word.exception.WordAnalyticException;
import ru.agima.testapp.word.model.FileProcessResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileReader extends Thread {

    private final int textBufferLimit = 100000;
    private int textBufferSize = 0;
    private final int processLimit;
    private final List<String> buffer;
    private final FileProcessResource resource;

    public FileReader(FileProcessResource resource) {
        this.resource = resource;
        this.processLimit = Runtime.getRuntime().availableProcessors() * 5;
        buffer = new ArrayList<>();
    }

    public void run() {
        try (Stream<Path> sourceRootFolder = Files.walk(resource.getSourceRootFolder())) {
            sourceRootFolder
                    .filter(Files::isRegularFile)
                    .filter(path -> !path.toFile().getAbsolutePath().contains("$RECYCLE"))
                    .filter(path -> path.getFileName().toFile().getName().toLowerCase().endsWith(".txt") || path.getFileName().toFile().getName().toLowerCase().endsWith(".xml"))
                    .forEach(this::readFile);
        } catch (IOException e) {
            throw new WordAnalyticException(e);
        }
    }

    private void readFile(Path path) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(path)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.add(line);
                for (; ; ) {
                    if (processLimit > resource.getQueue().size()) {
                        break;
                    }
                }
                textBufferSize += line.length();
                if (textBufferSize >= textBufferLimit) {
                    resource.getQueue().add(new ArrayList<>(buffer));
                    textBufferSize = 0;
                    buffer.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(MessageFormat.format("File {0} is ready in thread {1}", path.toFile().getName(), Thread.currentThread().getName()));
    }

}
