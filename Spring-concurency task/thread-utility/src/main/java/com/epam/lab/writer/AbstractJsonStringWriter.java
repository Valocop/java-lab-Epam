package com.epam.lab.writer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractJsonStringWriter implements Runnable, JsonStringWriter {
    private static final Logger LOG = LogManager.getLogger(AbstractJsonStringWriter.class);
    private static AtomicInteger fileNumber = new AtomicInteger(0);
    private volatile boolean isStop = false;
    private BlockingQueue<String> queue;
    private Path path;
    private int count;

    public AbstractJsonStringWriter(BlockingQueue<String> queue, Path path, int newsPerFile) {
        this.queue = queue;
        this.path = path;
        this.count = newsPerFile;
    }

    @Override
    public void stopWrite() {
        isStop = true;
    }

    @Override
    public void run() {
        try {
            while (true) {
                List<String> strings = new ArrayList<>();

                if (isStop) return;

                for (int i = 0; i < count; i++) {
                    String take = queue.take();
                    strings.add(take);
                }

                int fileNumber = AbstractJsonStringWriter.fileNumber.incrementAndGet();
                try {
                    write(path, "file" + fileNumber, strings);
                    LOG.info("Writer " + Thread.currentThread().getName() + " was write file" + fileNumber);
                } catch (IOException e) {
                    LOG.warn("Writer " + Thread.currentThread().getName() +
                            " couldn't write a file" + fileNumber + " by exception", e);
                }
            }
        } catch (InterruptedException e) {
            LOG.warn("Writer " + Thread.currentThread().getName() + " was stopped by InterruptedException exception");
        }
    }
}
