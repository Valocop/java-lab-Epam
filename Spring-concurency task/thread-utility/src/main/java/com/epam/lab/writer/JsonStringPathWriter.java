package com.epam.lab.writer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class JsonStringPathWriter extends AbstractJsonStringWriter {
    private static final Logger LOG = LogManager.getLogger(JsonStringWriter.class);

    public JsonStringPathWriter(BlockingQueue<String> queue, Path path, int newsPerFile) {
        super(queue, path, newsPerFile);
    }

    @Override
    public void write(Path path, String fileName, List<String> strings) throws IOException {
        File filePath = path.resolve(fileName).toFile();
        String jsonString = "[" +
                String.join(",", strings) +
                "]";

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw")) {
            FileChannel fileChannel = randomAccessFile.getChannel();
            fileChannel.lock();
            LOG.info("Lock was given by file " + filePath);
            fileChannel.write(ByteBuffer.wrap(jsonString.getBytes()));
            LOG.info("File was written to " + filePath);
        }
    }
}
