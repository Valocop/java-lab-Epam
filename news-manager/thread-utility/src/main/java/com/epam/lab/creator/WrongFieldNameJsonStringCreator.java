package com.epam.lab.creator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import static com.epam.lab.RandomNews.*;

public class WrongFieldNameJsonStringCreator extends AbstractStringJsonCreator {

    WrongFieldNameJsonStringCreator(BlockingQueue<String> queue, int count) {
        super(queue, count);
    }

    @Override
    public String createStringJson() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date randomCreationDate = getRandomCreationDate();
        return new StringBuilder().append("{")
                .append("\"title\"").append(":").append("\"").append(getUniqueNewsTitle(15)).append("\"").append(",")
                .append("\"shortText\"").append(":").append("\"").append(getRandomShortText(50)).append("\"").append(",")
                .append("\"full\"").append(":").append("\"").append(getRandomFullText(200)).append("\"").append(",")
                .append("\"creation\"").append(":").append("\"")
                .append(dateFormat.format(randomCreationDate)).append("\"").append(",")
                .append("\"modificationDate\"").append(":").append("\"")
                .append(dateFormat.format(getRandomModificationDate(randomCreationDate))).append("\"")
                .append("}").toString();
    }
}
