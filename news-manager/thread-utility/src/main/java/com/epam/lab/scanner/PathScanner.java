package com.epam.lab.scanner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.BlockingQueue;

public class PathScanner implements Runnable {
    private static final Logger LOG = LogManager.getLogger(PathScanner.class);
    private Path path;
    private BlockingQueue<Path> queue;

    public PathScanner(Path path, BlockingQueue<Path> queue) {
        this.path = path;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            while (true) {
                WatchKey key = watchService.take();

                for (WatchEvent<?> watchEvent : key.pollEvents()) {
                    Path context = (Path) watchEvent.context();
                    queue.put(path.resolve(context));
                    LOG.info(context + " was adding to reading queue by scanner " + Thread.currentThread().getName());
                }

                if (!key.reset()) break;
            }
        } catch (IOException | InterruptedException e) {
            LOG.warn("Scanner " + Thread.currentThread().getName() + " was stopped by exception", e);
        }
    }
}
