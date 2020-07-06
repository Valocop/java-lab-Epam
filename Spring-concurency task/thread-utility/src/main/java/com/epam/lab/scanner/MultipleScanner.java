package com.epam.lab.scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MultipleScanner {
    private static final Logger LOG = LogManager.getLogger(MultipleScanner.class);
    private ScheduledExecutorService scheduledExecutorService;
    private BlockingQueue<Path> queue;

    public MultipleScanner(BlockingQueue<Path> queue) {
        this.queue = queue;
    }

    public void startScan(List<Path> paths, int delay, TimeUnit timeUnit) {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(paths.size());
        LOG.info("MultipleScanner starts path scanners");
        paths.forEach(path -> {
            scheduledExecutorService.scheduleAtFixedRate(new PathScanner(path, queue), 0, delay, timeUnit);
        });
    }

    public void stopScan() {
        LOG.info("MultipleScanner stops path scanners");
        scheduledExecutorService.shutdownNow();
    }
}
