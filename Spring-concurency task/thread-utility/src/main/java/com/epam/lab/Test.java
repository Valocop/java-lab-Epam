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

//        for (int i = 0; i < 10; i++) {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date randomCreationDate = getRandomCreationDate();
//            validJsonList.add(new StringBuilder().append("{")
//                    .append("\"title\"").append(":").append("\"").append(getUniqueNewsTitle(15)).append("\"").append(",")
//                    .append("\"shortText\"").append(":").append("\"").append(getRandomShortText(50)).append("\"").append(",")
//                    .append("\"fullText\"").append(":").append("\"").append(getRandomFullText(250)).append("\"").append(",")
//                    .append("\"creationDate\"").append(":").append("\"")
//                    .append(dateFormat.format(randomCreationDate)).append("\"").append(",")
//                    .append("\"modificationDate\"").append(":").append("\"")
//                    .append(dateFormat.format(getRandomModificationDate(randomCreationDate))).append("\"").append(",")
//                    .append("\"author\"").append(":").append("{")
//                    .append("\"name\"").append(":").append("\"Andrei\"").append(",")
//                    .append("\"surname\"").append(":").append("\"Zdanov\"").append("}").append(",")
//                    .append("\"tags\"").append(":").append("[")
//                    .append("{").append("\"name\"").append(":").append("\"Tag1\"").append("}").append(",")
//                    .append("{").append("\"name\"").append(":").append("\"Tag2\"").append("}")
//                    .append("]").append("}").toString());
//        }

//        String jsonString = "[" + String.join(",", validJsonList) + "]";
//        Files.write(Paths.get("C:\\Users\\Admin\\Desktop\\epam\\task-12 Thread-utility\\root\\json"), jsonString.getBytes());
//
//        List<String> strings = Files.readAllLines(Paths.get("C:\\Users\\Admin\\Desktop\\epam\\task-12 Thread-utility\\root\\json"));
//        String readerJsonStrings = String.join("", strings);

        String readerJsonStrings = "[{\"title\":\"vsEJmRioTpvRbuQFsiIsyBBVEmAzPPTqZhs\",\"shortText\":\"2mfoKKsN8rC0dSzC1ua2XcMtSpy2SJhDMd56GGvSMKN2hwCsS692fCG9c9JN\",\"fullText\":\"dPckguXgAgtHvFMOhyJKN2F2zfifr6LHaHoP34CZtIqgDfTOoIqTd6o1aXhXrmvAobA14vSIGODeCvXHM5E1pd6Mb70uhLhD2D02ptbevvq1NwKGADi4GNGnHCcm6tyoLv1izuQIk32deBAMA8szljW965mripR5iNi6eG2cGHJq5Mql8aFaHE5k3i5K1eU6KSyALPaUfgVBKL818CsYwou6Ka5Ui9XOARvYwyEjnXxMP2TR7vlzCjDfxwBgFgTt1McVjzOwSxtnnKWzZQYQmuVKhheF7XYf21XG6SOao9h1\",\"creationDate\":\"2020-05-29\",\"modificationDate\":\"2020-06-06,\"surname\":\"Zdanov\"},\"tags\":[{\"name\":\"Tag1\"},{\"name\":\"Tag2\"}]},{\"title\":\"uLamTdfPAxjHbMmNePNVotkNQdUIXhyMskg\",\"shortText\":\"9R4dDkfgUxc1AxxEAGsfm863XabRefyP6rc8ZRsjf3vBsG9uWWznYuEVLSLN\",\"fullText\":\"tjmb9XlG0Ayovt3fwyv9pqturVckZ2ILgZrwoNJbXc0oc24Eu5FDX4aq0shJHWNWGJxYO3HkEYufCbBhOKhliI5h7ADbofTPJJvsF2V28NRDsR3yilYvshi7DvGmx3owhBbeAn2RTT0x9fADBm0Xk2YZVyMPwiM1Bsz78Um8Gpnwn79CHLejseqWdPVMZubGl80uruTE1xc90QO2pWme8yyU1u6S2udcBXLqLcol4mmAV6RtnUuT5upFrkQwcsvHI9ikjH5SI1ZogoYzuHRijcepbDpAD66lxBeNOHi63chg\",\"creationDate\":\"2020-05-28\",\"modificationDate\":\"2020-05-30,\"surname\":\"Zdanov\"},\"tags\":[{\"name\":\"Tag1\"},{\"name\":\"Tag2\"}]},{\"title\":\"lgVJpzjjNHOlJHvaQaAJjfDchnoAcgxKDle\",\"shortText\":\"drvhrTAsTZQCkDJ0ynbBdTR7kZP0X6HIcx2VRpPewkZwMvyLpygIMd7CvKkM\",\"fullText\":\"bB4FEXIW5KTzyhQDMAjtUdO9gswVIhIeJIOQfMnajl1iOBZM8Yu4tbRMIgFUekHzw6HtUgJlucVYxxbPDQDhfeARHd9tnscaWsXv0BZcAoX3Np1TixA06L9s81fLXBO3F9c7Gvx8GmGuKNHj1c76YxlzVHRmXsVxN2QAEFPCzLbDqBPLIMFR9SJIHWGuyJieQInFOYx7FBMjMi7Ea3nnoy9UF9hVWnry0XGcUHzy0xwGRhos6gg70ceBg5oAqaWaMDhi6cem1dYYAHDdjjjc6LvDGic9bttSRgozsZNvhd8m\",\"creationDate\":\"2020-05-24\",\"modificationDate\":\"2020-05-29,\"surname\":\"Zdanov\"},\"tags\":[{\"name\":\"Tag1\"},{\"name\":\"Tag2\"}]}]";

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
