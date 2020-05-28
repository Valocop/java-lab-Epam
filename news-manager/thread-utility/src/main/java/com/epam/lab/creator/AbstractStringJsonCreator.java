package com.epam.lab.creator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractStringJsonCreator extends Thread implements StringJsonCreator {
    private static AtomicInteger createdJsonCount = new AtomicInteger(0);
    private BlockingQueue<String> queue;
    private int count;

    protected AbstractStringJsonCreator(BlockingQueue<String> queue, int count) {
        this.queue = queue;
        this.count = count;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < count; i++) {
                if (interrupted()) break;
                queue.put(createStringJson());
                System.out.println("Json created to queue " + atomicInteger.incrementAndGet());
            }
        } catch (InterruptedException e) {
            System.out.println("==========JsonCreator ended by InterruptedException===========");
            return;
        }
        System.out.println("===========JsonCreator is completed successfully===========");
    }
}
