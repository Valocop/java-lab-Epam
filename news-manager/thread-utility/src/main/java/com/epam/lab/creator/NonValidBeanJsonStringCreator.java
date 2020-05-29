package com.epam.lab.creator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import static com.epam.lab.RandomNews.*;

public class NonValidBeanJsonStringCreator extends AbstractStringJsonCreator {

    public NonValidBeanJsonStringCreator(BlockingQueue<String> queue, int count) {
        super(queue, count);
    }

    @Override
    public String createStringJson() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date randomCreationDate = getRandomCreationDate();
        return new StringBuilder().append("{")
                .append("\"title\"").append(":").append("\"").append(getUniqueNewsTitle(20)).append("\"").append(",")
                .append("\"shortText\"").append(":").append("\"").append(getRandomShortText(150)).append("\"").append(",")
                .append("\"fullText\"").append(":").append("\"").append(getRandomFullText(250)).append("\"").append(",")
                .append("\"creationDate\"").append(":").append("\"").append(dateFormat.format(randomCreationDate))
                .append("\"").append(",")
                .append("\"modificationDate\"").append(":").append("\"")
                .append(dateFormat.format(getRandomModificationDate(randomCreationDate))).append("\"")
                .append("}").toString();
    }
}
