package com.epam.lab.creator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractJsonStringCreator extends Thread implements JsonStringCreator {
    private static final Logger LOG = LogManager.getLogger(AbstractJsonStringCreator.class);
    private static AtomicInteger createdJsonCount = new AtomicInteger(0);
    private BlockingQueue<String> queue;
    private int count;

    AbstractJsonStringCreator(BlockingQueue<String> queue, int count) {
        this.queue = queue;
        this.count = count;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < count; i++) {
                if (interrupted()) {
                    LOG.info("JsonCreator " + Thread.currentThread().getName() + " was stopped by interrupt");
                    break;
                }
                queue.put(createStringJson());
                LOG.info("Json string " + createdJsonCount.incrementAndGet() + " was added to queue by " + Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            LOG.warn("JsonCreator " + Thread.currentThread().getName() + " was stopped by InterruptedException");
        }
    }
}
