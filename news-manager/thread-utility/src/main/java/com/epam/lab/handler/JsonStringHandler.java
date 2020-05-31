package com.epam.lab.handler;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.validator.JsonStringValidator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class JsonStringHandler implements Runnable {
    private static final Logger LOG = LogManager.getLogger(JsonStringHandler.class);
    private BlockingQueue<Path> paths;
    private ObjectMapper objectMapper;
    private JsonStringValidator jsonStringValidator;
    private volatile boolean isStop = false;

    public JsonStringHandler(BlockingQueue<Path> paths, ObjectMapper objectMapper, JsonStringValidator validator) {
        this.paths = paths;
        this.objectMapper = objectMapper;
        this.jsonStringValidator = validator;
    }

    public void stop() {
        isStop = true;
    }

    @Override
    public void run() {
        try {
            while (true) {

                if (isStop) {
                    LOG.info("JsonStringHandler " + Thread.currentThread().getName() + " was stopped by stop");
                    return;
                }

                Path takenPath = paths.take();
                List<String> readerStrings = Files.readAllLines(takenPath);
                LOG.info("Path " + takenPath + " was reading by JsonStringHandler " + Thread.currentThread().getName());

                String jsonString = String.join("", readerStrings);
                List<NewsDto> news = objectMapper.readValue(jsonString, new TypeReference<List<NewsDto>>() {
                });

                boolean isValid = jsonStringValidator.validate(news);
                if (isValid) {
                    LOG.info("File " + takenPath.toString() + " is valid");
                } else {
                    LOG.info("File " + takenPath.toString() + " is not valid");
                }
            }
        } catch (InterruptedException | IOException e) {
            LOG.warn("JsonStringHandler " + Thread.currentThread().getName() + " was stopped by exception", e);
        }
    }
}
