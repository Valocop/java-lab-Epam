package com.epam.lab.handler;

import com.epam.lab.validator.JsonStringValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class MultipleJsonStringHandler {
    private static final Logger LOG = LogManager.getLogger(MultipleJsonStringHandler.class);
    private int threadCount;
    private ScheduledExecutorService scheduledExecutorService;
    private BlockingQueue<Path> paths;
    private ObjectMapper objectMapper;
    private JsonStringValidator jsonStringValidator;

    @Autowired
    public MultipleJsonStringHandler(BlockingQueue<Path> paths, ObjectMapper objectMapper, JsonStringValidator jsonStringValidator,
                                     @Value("${THREAD_COUNT}") int threadCount) {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(threadCount);
        this.threadCount = threadCount;
        this.paths = paths;
        this.objectMapper = objectMapper;
        this.jsonStringValidator = jsonStringValidator;
    }

    @Autowired
    public void startJsonStringHandler(@Value("${SCAN_DELAY}") int period,
                                       @Value("#{T(java.util.concurrent.TimeUnit).MICROSECONDS}") TimeUnit timeUnit) {
        LOG.info("MultipleJsonStringHandler stars JsonStringHandlers");
        for (int i = 0; i < threadCount; i++) {
            scheduledExecutorService.scheduleAtFixedRate(
                    new JsonStringHandler(paths, objectMapper, jsonStringValidator), 0, period, timeUnit);
        }
    }

    public void stopJsonStringHandler() {
        LOG.info("MultipleJsonStringHandler stops JsonStringHandlers");
        scheduledExecutorService.shutdownNow();
    }
}
