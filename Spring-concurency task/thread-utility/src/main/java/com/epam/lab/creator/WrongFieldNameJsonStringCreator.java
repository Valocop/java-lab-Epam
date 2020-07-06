package com.epam.lab.creator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import static com.epam.lab.random.RandomNews.*;

public class WrongFieldNameJsonStringCreator extends AbstractJsonStringCreator {

    public WrongFieldNameJsonStringCreator(BlockingQueue<String> queue, int count) {
        super(queue, count * 3 / 20);
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
                .append(dateFormat.format(getRandomModificationDate(randomCreationDate))).append(",")
                .append("\"author\"").append(":").append("{")
                .append("\"name\"").append(":").append("\"Andrei\"").append(",")
                .append("\"surname\"").append(":").append("\"Zdanov\"").append("}").append(",")
                .append("\"tags\"").append(":").append("[")
                .append("{").append("\"name\"").append(":").append("\"Tag1\"").append("}").append(",")
                .append("{").append("\"name\"").append(":").append("\"Tag2\"").append("}")
                .append("]").append("}").toString();
    }
}
