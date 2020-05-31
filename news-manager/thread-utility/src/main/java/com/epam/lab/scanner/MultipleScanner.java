package com.epam.lab.scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MultipleScanner {
    private static final Logger LOG = LogManager.getLogger(MultipleScanner.class);
    private ScheduledExecutorService scheduledExecutorService;
    private List<Path> paths;
    private BlockingQueue<Path> queue;

    @Autowired
    public MultipleScanner(@Value("#{subFolderCreatorImpl.createdPaths}") List<Path> paths, BlockingQueue<Path> queue) {
        this.scheduledExecutorService = Executors.newScheduledThreadPool(paths.size());
        this.paths = paths;
        this.queue = queue;
    }

    @Autowired
    public void startScan(@Value("${SCAN_DELAY}") int delay,
                          @Value("#{T(java.util.concurrent.TimeUnit).MICROSECONDS}") TimeUnit timeUnit) {
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
