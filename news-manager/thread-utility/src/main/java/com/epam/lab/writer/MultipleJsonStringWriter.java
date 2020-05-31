package com.epam.lab.writer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

@Component
public class MultipleJsonStringWriter {
    private static final Logger LOG = LogManager.getLogger(MultipleJsonStringWriter.class);
    private List<JsonStringPathWriter> jsonStringWriters = new ArrayList<>();
    private CyclicBarrier cyclicBarrier;
    private List<Path> paths;

    public MultipleJsonStringWriter(@Value("#{subFolderCreatorImpl.createdPaths}") List<Path> paths,
                                    @Value("${PERIOD_TIME}") int period,
                                    @Value("#{T(java.util.concurrent.TimeUnit).MILLISECONDS}") TimeUnit timeUnit) {
        this.paths = paths;
        this.cyclicBarrier = new CyclicBarrier(paths.size(), () -> {
            try {
                LOG.info("CyclicBarrier is waiting period time " + period);
                timeUnit.sleep(period);
            } catch (InterruptedException e) {
                LOG.info("CyclicBarrier was stopped by Exception", e);
            }
        });
    }

    @Autowired
    public void startWriting(BlockingQueue<String> queue, @Value("3") int newsPerFile) {
        LOG.info("MultipleJsonStringWriter starts write json files to queue");
        paths.forEach(path -> jsonStringWriters.add(new JsonStringPathWriter(queue, cyclicBarrier, path, newsPerFile)));
        jsonStringWriters.forEach(Thread::start);
    }

    public void stopWriting() {
        LOG.info("MultipleJsonStringWriter trying to stop writing json files to queue");
        jsonStringWriters.forEach(AbstractJsonStringWriter::stopWrite);
        cyclicBarrier.reset();
    }
}
