package com.epam.lab.random;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RandomNews {
    private static Set<String> uniqueTitles = new HashSet<>();
    private static Faker faker = new Faker();

    public static String getUniqueNewsTitle(int length) {
        String title = RandomStringUtils.random(length, true, false);
        if (!uniqueTitles.contains(title)) {
            uniqueTitles.add(title);
            return title;
        } else {
            return getUniqueNewsTitle(length);
        }
    }

    public static String getRandomShortText(int length) {
        return RandomStringUtils.random(length, true, true);
    }

    public static String getRandomFullText(int length) {
        return RandomStringUtils.random(length, true, true);
    }

    public static Date getRandomCreationDate() {
        return faker.date().past(20, TimeUnit.DAYS);
    }

    public static Date getRandomModificationDate(Date futureDate) {
        return faker.date().future(10, TimeUnit.DAYS, futureDate);
    }
}
