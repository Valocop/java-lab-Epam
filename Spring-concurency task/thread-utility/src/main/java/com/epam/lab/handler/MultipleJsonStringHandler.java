package com.epam.lab.handler;

import com.epam.lab.service.NewsService;
import com.epam.lab.validator.JsonStringValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class MultipleJsonStringHandler {
    private static final Logger LOG = LogManager.getLogger(MultipleJsonStringHandler.class);
    private int threadCount;
    private ScheduledExecutorService scheduledExecutorService;
    private ObjectMapper objectMapper;
    private JsonStringValidator jsonStringValidator;
    private Map<Path, HandlerStatus> handlerStatusMap = new ConcurrentHashMap<>();

    public MultipleJsonStringHandler(ObjectMapper objectMapper, JsonStringValidator jsonStringValidator, int threadCount) {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(threadCount);
        this.threadCount = threadCount;
        this.objectMapper = objectMapper;
        this.jsonStringValidator = jsonStringValidator;
    }

    public void startJsonStringHandler(BlockingQueue<Path> paths, NewsService newsService,
                                       Path removedPath, int period, TimeUnit timeUnit) {
        LOG.info("MultipleJsonStringHandler stars JsonStringHandlers");
        for (int i = 0; i < threadCount; i++) {
            JsonStringHandler jsonStringHandler = new JsonStringHandler(
                    paths, removedPath, objectMapper, newsService, jsonStringValidator, handlerStatusMap);
            scheduledExecutorService.scheduleAtFixedRate(jsonStringHandler, 0, period, timeUnit);
        }
    }

    public void stopJsonStringHandler() {
        LOG.info("MultipleJsonStringHandler stops JsonStringHandlers");
        scheduledExecutorService.shutdownNow();
    }

    public List<Path> getAddedToDbPaths() {
        return handlerStatusMap.entrySet().stream()
                .filter(pathHandlerStatusEntry -> pathHandlerStatusEntry.getValue() == HandlerStatus.ADDED)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Path> getRemovedPaths() {
        return handlerStatusMap.entrySet().stream()
                .filter(pathHandlerStatusEntry -> pathHandlerStatusEntry.getValue() == HandlerStatus.DELETED)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    enum HandlerStatus {
        DELETED, ADDED
    }
}
