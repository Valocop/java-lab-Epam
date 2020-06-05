package com.epam.lab;

import com.epam.lab.configuration.SpringThreadUtilityConfiguration;
import com.epam.lab.thread.ThreadUtility;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class Main {

    public static void main(String[] args) {
        GenericApplicationContext context = new AnnotationConfigApplicationContext(SpringThreadUtilityConfiguration.class);
        ThreadUtility threadUtility = (ThreadUtility) context.getBean("threadUtility");
        threadUtility.startUtility();
    }
}