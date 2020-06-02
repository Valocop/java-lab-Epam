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
    private BlockingQueue<Path> pathBlockingQueue;
    private ObjectMapper objectMapper;
    private JsonStringValidator jsonStringValidator;
    private volatile boolean isStop = false;

    public JsonStringHandler(BlockingQueue<Path> pathBlockingQueue, ObjectMapper objectMapper, JsonStringValidator validator) {
        this.pathBlockingQueue = pathBlockingQueue;
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
                LOG.info(Thread.currentThread().getName() + " starts to work");
                if (isStop) {
                    LOG.info("JsonStringHandler " + Thread.currentThread().getName() + " was stopped by stop flag");
                    return;
                }

                Path takenPath = pathBlockingQueue.take();
                LOG.info(takenPath + " file was taken from queue by " + Thread.currentThread().getName());

                try {
                    byte[] allBytes = Files.readAllBytes(takenPath);
                    String jsonString = new String(allBytes);
                    validateFile(takenPath, jsonString);
                    LOG.info(takenPath + " was read by " + Thread.currentThread().getName());
                } catch (IOException e) {
                    LOG.info(takenPath + " couldn't read by exception: " + e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            LOG.warn("JsonStringHandler " + Thread.currentThread().getName() + " was stopped by exception", e);
        }
    }

    private void validateFile(Path takenPath, String jsonString) {
        boolean isValid = false;
        try {
            List<NewsDto> news = objectMapper.readValue(jsonString, new TypeReference<List<NewsDto>>() {
            });
            isValid = jsonStringValidator.validate(news);
        } catch (IOException e) {
            LOG.error("Json file " + takenPath.getFileName() + " is not valid by exception: " + e.getMessage());
        }

        if (isValid) {
            LOG.info("File " + takenPath.getFileName() + " is valid");
        } else {
            LOG.info("File " + takenPath.getFileName() + " is not valid:\n" + jsonString);
        }
    }
}
