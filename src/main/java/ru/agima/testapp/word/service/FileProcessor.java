package ru.agima.testapp.word.service;

import ru.agima.testapp.word.model.ProcessFileRequest;
import ru.agima.testapp.word.model.FileProcessResource;
import ru.agima.testapp.word.thread.CustomThreadPool;

import java.nio.file.Files;
import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class FileProcessor {

    private final Consumer<FileProcessResource> postProcessor;

    public FileProcessor(Consumer<FileProcessResource> postProcessor) {
        this.postProcessor = postProcessor;
    }

    public void execute(ProcessFileRequest request) {
        if (Files.notExists(request.getTarget())) {
            throw new IllegalArgumentException(String.format("File not found %s", request.getTarget().toAbsolutePath()));
        }
        System.out.println("STARTED..");
        Pattern pattern = Pattern.compile(String.format("(\\b[А-Яа-яA-Za-z]*[А-Яа-яA-Za-z\\d-]{%d}\\b)", request.getMinWordLength()));
        FileProcessResource resource = new FileProcessResource(pattern, request.getTarget());
        CustomThreadPool customThreadPool = new CustomThreadPool(resource);
        FileReader fileReader = new FileReader(resource);
        fileReader.start();
        try {
            fileReader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            customThreadPool.awaitTermination(2, ChronoUnit.SECONDS);
        }
        postProcessor.accept(resource);
    }

}
