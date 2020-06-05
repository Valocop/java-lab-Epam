package com.epam.lab.thread;

import com.epam.lab.creator.*;
import com.epam.lab.handler.MultipleJsonStringHandler;
import com.epam.lab.scanner.MultipleScanner;
import com.epam.lab.service.NewsService;
import com.epam.lab.validator.JsonStringValidatorImpl;
import com.epam.lab.writer.MultipleJsonStringWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Component("threadUtility")
public class ThreadUtility {
    private static final Logger LOG = LogManager.getLogger(ThreadUtility.class);
    @Value("${FILES_COUNT}")
    private int filesCount;
    @Value("${THREAD_COUNT}")
    private int threadCount;
    @Value("${JSON_ROOT_PATH}")
    private String path;
    @Value("${JSON_REMOVED_PATH}")
    private String removedPath;
    @Value("${SUBORDERS_COUNT}")
    private int subFolderCount;
    @Value("${PERIOD_TIME}")
    private int writePeriod;
    @Value("${SCAN_DELAY}")
    private int scanPeriod;
    @Value("${TEST_TIME}")
    private int testTime;
    @Value("${FOLDERS_DEEP}")
    private int foldersDeep;
    @Value("${NEWS_PER_FILE}")
    private int newsPerFile;
    private BlockingQueue<String> readingQueue;
    private BlockingQueue<Path> pathsQueue;
    private MultipleJsonStringCreator multipleJsonStringCreator;
    private MultipleJsonStringWriter multipleJsonStringWriter;
    private MultipleJsonStringHandler multipleJsonStringHandler;
    private MultipleScanner multipleScanner;
    private ObjectMapper objectMapper;
    private Validator validator;
    private NewsService newsService;

    @Autowired
    public ThreadUtility(ObjectMapper objectMapper, Validator validator, NewsService newsService) {
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.newsService = newsService;
    }

    @PostConstruct
    public void init() {
        readingQueue = new ArrayBlockingQueue<>(filesCount);
        pathsQueue = new ArrayBlockingQueue<>(filesCount);
        List<AbstractJsonStringCreator> jsonCreatorList = Arrays.asList(
                new NonValidBeanJsonStringCreator(readingQueue, filesCount),
                new ValidJsonStringCreator(readingQueue, filesCount),
                new ViolatesDbConstraintsJsonStringCreator(readingQueue, filesCount),
                new WrongFieldNameJsonStringCreator(readingQueue, filesCount),
                new WrongJsonStringCreator(readingQueue, filesCount));
        multipleJsonStringCreator = new MultipleJsonStringCreator(jsonCreatorList);
        multipleJsonStringWriter = new MultipleJsonStringWriter();
        multipleJsonStringHandler = new MultipleJsonStringHandler(objectMapper,
                new JsonStringValidatorImpl(validator), threadCount);
        multipleScanner = new MultipleScanner(pathsQueue);
        long before = newsService.getCountOfNews();
        startUtility();
        checkResult(before);
    }

    public void startUtility() {
        SubFolderCreator subFolderCreator = new SubFolderCreatorImpl();
        try {
            List<Path> paths = subFolderCreator.create(
                    Paths.get(path), Paths.get(removedPath), subFolderCount, foldersDeep);
            multipleJsonStringCreator.startCreating();
            multipleJsonStringWriter.startWriting(paths, readingQueue, newsPerFile, writePeriod, TimeUnit.MILLISECONDS);
            multipleScanner.startScan(paths, scanPeriod, TimeUnit.MICROSECONDS);
            multipleJsonStringHandler.startJsonStringHandler(
                    pathsQueue, newsService, Paths.get(removedPath), scanPeriod, TimeUnit.MICROSECONDS);
            Thread.sleep(testTime);
            stopUtility();
            Thread.sleep(500);
        } catch (IOException | InterruptedException e) {
            LOG.error("ThreadUtility was stopped by exception", e);
        }
    }

    public void checkResult(long before) {
        long after = newsService.getCountOfNews();
        long created = after - before;
        List<Path> addedToDbPaths = multipleJsonStringHandler.getAddedToDbPaths();
        List<Path> removedPaths = multipleJsonStringHandler.getRemovedPaths();
        LOG.info("Count of probably added to database files: " + addedToDbPaths.size());
        LOG.info("Count of not valid and removed files: " + removedPaths.size());
        LOG.info("Count of added to database files: " + created);
    }

    private void stopUtility() {
        multipleJsonStringCreator.stopCreating();
        multipleJsonStringWriter.stopWriting();
        multipleScanner.stopScan();
        multipleJsonStringHandler.stopJsonStringHandler();
    }
}
