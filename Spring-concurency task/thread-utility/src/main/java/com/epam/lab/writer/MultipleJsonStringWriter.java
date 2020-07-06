package com.epam.lab.writer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultipleJsonStringWriter {
    private static final Logger LOG = LogManager.getLogger(MultipleJsonStringWriter.class);
    private ScheduledExecutorService scheduledExecutorService;

    public void startWriting(List<Path> paths, BlockingQueue<String> queue, int newsPerFile, int period, TimeUnit timeUnit) {
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(paths.size());
        paths.forEach(path -> scheduledExecutorService.scheduleAtFixedRate(
                new JsonStringPathWriter(queue, path, newsPerFile), 0, period, timeUnit));
    }

    public void stopWriting() {
        LOG.info("MultipleJsonStringWriter trying to stop writing json files to queue");
        scheduledExecutorService.shutdownNow();
    }
}
