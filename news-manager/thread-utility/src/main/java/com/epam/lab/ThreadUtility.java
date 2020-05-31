package com.epam.lab;

import com.epam.lab.creator.MultipleJsonStringCreator;
import com.epam.lab.handler.MultipleJsonStringHandler;
import com.epam.lab.writer.MultipleJsonStringWriter;
import org.springframework.stereotype.Component;

@Component
public class ThreadUtility {
    private MultipleJsonStringCreator multipleJsonStringCreator;
    private MultipleJsonStringWriter multipleJsonStringWriter;
    private MultipleJsonStringHandler multipleJsonStringHandler;

    public ThreadUtility() {
//        multipleJsonStringCreator = new MultipleJsonStringCreator();
    }
}
