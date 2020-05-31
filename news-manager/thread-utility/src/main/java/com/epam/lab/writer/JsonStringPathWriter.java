package com.epam.lab.writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;

public class JsonStringPathWriter extends AbstractJsonStringWriter {

    public JsonStringPathWriter(BlockingQueue<String> queue, CyclicBarrier cyclicBarrier, Path path, int count) {
        super(queue, cyclicBarrier, path, count);
    }

    @Override
    public void write(Path path, String fileName, List<String> strings) throws IOException {
        Path filePath = path.resolve(fileName);
        String jsonString = "[" +
                String.join(",", strings) +
                "]";
        Files.write(filePath, jsonString.getBytes());
    }
}
