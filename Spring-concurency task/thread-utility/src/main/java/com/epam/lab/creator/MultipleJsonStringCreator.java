package com.epam.lab.creator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

public class MultipleJsonStringCreator {
    private static final Logger LOG = LogManager.getLogger(MultipleJsonStringCreator.class);
    private List<AbstractJsonStringCreator> jsonCreatorList;

    public MultipleJsonStringCreator(List<AbstractJsonStringCreator> jsonCreatorList) {
        this.jsonCreatorList = jsonCreatorList;
    }

    public void startCreating() {
        LOG.info("Starting to create json string to queue");
        jsonCreatorList.forEach(Thread::start);
    }

    public void stopCreating() {
        LOG.info("Stopping to create json string to queue");
        jsonCreatorList.forEach(Thread::interrupt);
    }
}
