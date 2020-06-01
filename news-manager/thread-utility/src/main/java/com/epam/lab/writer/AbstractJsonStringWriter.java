package com.epam.lab.writer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractJsonStringWriter extends Thread implements JsonStringWriter {
    private static final Logger LOG = LogManager.getLogger(AbstractJsonStringWriter.class);
    private static AtomicInteger fileNumber = new AtomicInteger(0);
    private volatile boolean isStop = false;
    private BlockingQueue<String> queue;
    private CyclicBarrier cyclicBarrier;
    private Path path;
    private int count;

    public AbstractJsonStringWriter(BlockingQueue<String> queue, CyclicBarrier cyclicBarrier, Path path, int newsPerFile) {
        this.queue = queue;
        this.cyclicBarrier = cyclicBarrier;
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
                LOG.info("Writer " + Thread.currentThread().getName() + " is stopping on barrier.");
                cyclicBarrier.await();

                if (isStop) {
                    cyclicBarrier.reset();
                    LOG.info("Writer " + Thread.currentThread().getName() + " was stopped by stop. Barrier was reset.");
                    return;
                }

                for (int i = 0; i < count; i++) {
                    String take = queue.take();
                    strings.add(take);
                }
                int fileNumber = AbstractJsonStringWriter.fileNumber.incrementAndGet();
                write(path, "file" + fileNumber, strings);
                LOG.info("Writer " + Thread.currentThread().getName() + " was write file " + fileNumber);
            }
        } catch (InterruptedException | BrokenBarrierException | IOException e) {
            LOG.warn("Writer " + Thread.currentThread().getName() + " was stopped by exception", e);
        }
    }
}
