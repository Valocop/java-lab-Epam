package com.epam.lab.handler;

import com.epam.lab.validator.JsonStringValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultipleJsonStringHandler {
    private static final Logger LOG = LogManager.getLogger(MultipleJsonStringHandler.class);
    private int threadCount;
    private ScheduledExecutorService scheduledExecutorService;
    private ObjectMapper objectMapper;
    private JsonStringValidator jsonStringValidator;

    public MultipleJsonStringHandler(ObjectMapper objectMapper, JsonStringValidator jsonStringValidator, int threadCount) {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(threadCount);
        this.threadCount = threadCount;
        this.objectMapper = objectMapper;
        this.jsonStringValidator = jsonStringValidator;
    }

    public void startJsonStringHandler(BlockingQueue<Path> paths, Path removedPath, int period, TimeUnit timeUnit) {
        LOG.info("MultipleJsonStringHandler stars JsonStringHandlers");
        for (int i = 0; i < threadCount; i++) {
            scheduledExecutorService.scheduleAtFixedRate(
                    new JsonStringHandler(paths, removedPath, objectMapper, jsonStringValidator), 0, period, timeUnit);
        }
    }

    public void stopJsonStringHandler() {
        LOG.info("MultipleJsonStringHandler stops JsonStringHandlers");
        scheduledExecutorService.shutdownNow();
    }
}
