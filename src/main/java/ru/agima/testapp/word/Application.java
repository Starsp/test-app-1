package ru.agima.testapp.word;

import ru.agima.testapp.word.model.ProcessFileRequest;
import ru.agima.testapp.word.service.FilePostProcessor;
import ru.agima.testapp.word.service.FileProcessor;

import java.nio.file.Paths;
import java.text.MessageFormat;

public class Application {

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            String msg = String.format("Expected 2 arguments but found %d", args == null ? 0 : args.length);
            throw new IllegalArgumentException(msg);
        }
        long l = System.currentTimeMillis();
        ProcessFileRequest processFileRequest = new ProcessFileRequest(Paths.get(args[0]), Integer.valueOf(args[1]));
        FilePostProcessor filePostProcessor = new FilePostProcessor();
        FileProcessor processor = new FileProcessor(filePostProcessor::printTop);
        processor.execute(processFileRequest);
        System.out.println(MessageFormat.format("Finished at {0} mills", System.currentTimeMillis() - l));
    }

}
