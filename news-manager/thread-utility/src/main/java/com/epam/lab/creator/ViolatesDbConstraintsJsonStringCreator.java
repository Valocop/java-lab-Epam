package com.epam.lab.creator;

import com.epam.lab.random.RandomNews;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import static com.epam.lab.random.RandomNews.*;

@Component
public class ViolatesDbConstraintsJsonStringCreator extends AbstractJsonStringCreator {

    ViolatesDbConstraintsJsonStringCreator(BlockingQueue<String> queue, @Value("${FILES_COUNT}") int count) {
        super(queue, count / 20);
    }

    @Override
    public String createStringJson() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date randomCreationDate = getRandomCreationDate();
        return new StringBuilder().append("{")
                .append("\"title\"").append(":").append("\"").append(getUniqueNewsTitle(35)).append("\"").append(",")
                .append("\"shortText\"").append(":").append("\"").append(RandomNews.getRandomShortText(60)).append("\"").append(",")
                .append("\"fullText\"").append(":").append("\"").append(getRandomFullText(300)).append("\"").append(",")
                .append("\"creationDate\"").append(":").append("\"")
                .append(dateFormat.format(randomCreationDate)).append("\"").append(",")
                .append("\"modificationDate\"").append(":").append("\"")
                .append(dateFormat.format(getRandomModificationDate(randomCreationDate))).append(",")
                .append("\"surname\"").append(":").append("\"Zdanov\"").append("}").append(",")
                .append("\"tags\"").append(":").append("[")
                .append("{").append("\"name\"").append(":").append("\"Tag1\"").append("}").append(",")
                .append("{").append("\"name\"").append(":").append("\"Tag2\"").append("}")
                .append("]").append("}").toString();
    }
}
