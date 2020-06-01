package com.epam.lab;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.validator.JsonStringValidator;
import com.epam.lab.validator.JsonStringValidatorImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.epam.lab.random.RandomNews.*;

public class Test {

    public static void main(String[] args) throws IOException {
        List<String> validJsonList = new ArrayList<>();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator valid = validatorFactory.getValidator();
        JsonStringValidator validator = new JsonStringValidatorImpl(valid);
        ObjectMapper objectMapper = getObjectMapper();

        for (int i = 0; i < 10; i++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date randomCreationDate = getRandomCreationDate();
            validJsonList.add(new StringBuilder().append("{")
                    .append("\"title\"").append(":").append("\"").append(getUniqueNewsTitle(15)).append("\"").append(",")
                    .append("\"shortText\"").append(":").append("\"").append(getRandomShortText(50)).append("\"").append(",")
                    .append("\"fullText\"").append(":").append("\"").append(getRandomFullText(250)).append("\"").append(",")
                    .append("\"creationDate\"").append(":").append("\"")
                    .append(dateFormat.format(randomCreationDate)).append("\"").append(",")
                    .append("\"modificationDate\"").append(":").append("\"")
                    .append(dateFormat.format(getRandomModificationDate(randomCreationDate))).append("\"").append(",")
                    .append("\"author\"").append(":").append("{")
                    .append("\"name\"").append(":").append("\"Andrei\"").append(",")
                    .append("\"surname\"").append(":").append("\"Zdanov\"").append("}").append(",")
                    .append("\"tags\"").append(":").append("[")
                    .append("{").append("\"name\"").append(":").append("\"Tag1\"").append("}").append(",")
                    .append("{").append("\"name\"").append(":").append("\"Tag2\"").append("}")
                    .append("]").append("}").toString());
        }

//        String jsonString = "[" + String.join(",", validJsonList) + "]";
//        Files.write(Paths.get("C:\\Users\\Admin\\Desktop\\epam\\task-12 Thread-utility\\root\\json"), jsonString.getBytes());
//
//        List<String> strings = Files.readAllLines(Paths.get("C:\\Users\\Admin\\Desktop\\epam\\task-12 Thread-utility\\root\\json"));
//        String readerJsonStrings = String.join("", strings);

        String readerJsonStrings = "[{\"title\":\"gWuYGeMPnzFPIBn\",\"shortText\":\"IFvCC82hrSDqS7mZWqutQsHvOT9BiJnIxZsDgbhaR7U7AhQB8z\",\"fullText\":\"d59qxO7g69TbAyPidm9hvHmnOHuzGRR6VqfvqiuxDRAHXXeTB3tBP45qsqrx59umbg1dXDKBeWVoKmjHYFzykG3HWgqBwJizDisyIrM93Ky1K8yWtBQT60bzYi0LqQy1Vbfi5xUjUeuT9DKmtgCkbq6zvoCXFXRsWKGEwIIMiDJh3Y6JRHL4Uj3kBgF4zJYFmah7gdz5zjiVpV63O3vAtu1vBhtVIEr2kJBxZFhx6Ps3XXrKSfFmYsXtSB\",\"creationDate\":\"2020-05-13\",\"modificationDate\":\"2020-05-23\",\"author\":{\"name\":\"Andrei\",\"surname\":\"Zdanov\"},\"tags\":[{\"name\":\"Tag1\"},{\"name\":\"Tag2\"}]},{\"title\":\"ZynPsCUFTrpaeqK\",\"shortText\":\"IzG3sk7MZ3mkQxi0jM7gI4BeOHls5ApBYBAHDA7Ha1nRFuSqLv\",\"fullText\":\"Yj74A01FfFJ4lNyMkAji8oXkdlPcni8NCNZ6heRJCzTny5GWo5Cb0QDcEPVWeXJ7RwBidxtkTD9fPshInkFB4aM3wzkSPnoutNFk3VIUp0M6clFORXI0p1TcQXlAKbi3io6fxUrDKVffCgKuKJzN0rgNHGxDv2SbIVABA5Fo2RVlznbC2MCeLtazi38OIW4t6DSK8HrchdYpGu5YbRuwwp548NFXTXFAgndpFFIk06LmFVJVXNnczP3OPB\",\"creationDate\":\"2020-05-12\",\"modificationDate\":\"2020-05-19\",\"author\":{\"name\":\"Andrei\",\"surname\":\"Zdanov\"},\"tags\":[{\"name\":\"Tag1\"},{\"name\":\"Tag2\"}]},{\"title\":\"rrpJEfsoKJsHJrs\",\"shortText\":\"aCIxoOugENFMHiiK7Q0DhGKHvQjIvAmv0ZFiesn67AeWdNbZcT\",\"fullText\":\"WA88ZduyT2oZDmSBm8TzRvctgtzlNFz0ktx9K7oAdz2Zlp9bYZCRvKILI6ikMR4PaksG4Aqdkg4DKEEpd04iWvc2h7jSlcFQRBe0jMgZyV0RkpChYg75ibbywFCeJVsbnBE2swZOurwv1zuv2AgIQT63OYJV2hIMi7UBz1kNu5X5WhCoWooPR7YQWAPAsmnEMmzyFLc5FfSLEQfIb7c04lZnsUswk5PtHC98lLubkCI3IuFNdER6rQ3dgW\",\"creationDate\":\"2020-05-21\",\"modificationDate\":\"2020-05-22\",\"author\":{\"name\":\"Andrei\",\"surname\":\"Zdanov\"},\"tags\":[{\"name\":\"Tag1\"},{\"name\":\"Tag2\"}]}]";

        try {
            List<NewsDto> news = objectMapper.readValue(readerJsonStrings, new TypeReference<List<NewsDto>>() {
            });
            boolean validate = validator.validate(news);
            System.out.println(validate);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
}
