package com.epam.lab.dao;

import com.epam.lab.model.Author;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RunWith(JUnit4.class)
public class AuthorDaoImplTest {
    private static final String DB_PROPERTIES = "database.properties";
    private static final String CREATE_DB = "createDB.sql";
    private static final String DROP_DB = "dropDB.sql";
    private static JdbcTemplate jdbcTemplate;
    private static AuthorDaoImpl authorDao;

    @BeforeClass
    public static void setupJdbcTemplate() throws IOException {
        try (InputStream inputStream = AuthorDaoImplTest.class.getClassLoader().getResourceAsStream(DB_PROPERTIES)) {
            Properties properties = new Properties();
            properties.load(inputStream);

            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(properties.getProperty("driver"));
            dataSource.setUrl(properties.getProperty("url"));
            dataSource.setUsername(properties.getProperty("user"));
            dataSource.setPassword(properties.getProperty("password"));

            jdbcTemplate = new JdbcTemplate(dataSource);
            authorDao = new AuthorDaoImpl(jdbcTemplate);
        }
    }

    @Before
    public void initDatabase() throws IOException {
        Path path = Paths.get("src", "test", "resources", CREATE_DB);
        byte[] bytes = Files.readAllBytes(path);
        List<String> queryList = new ArrayList<>();
        ScriptUtils.splitSqlScript(new String(bytes), ";", queryList);
        queryList.forEach(s -> {
            jdbcTemplate.execute(s);
        });
    }

    @After
    public void dropDatabase() throws IOException {
        Path path = Paths.get("src", "test", "resources", DROP_DB);
        byte[] bytes = Files.readAllBytes(path);
        List<String> queryList = new ArrayList<>();
        ScriptUtils.splitSqlScript(new String(bytes), ";", queryList);
        queryList.forEach(s -> {
            jdbcTemplate.execute(s);
        });
    }

    @Test
    public void shouldSaveAuthor() {
        Author expectedAuthor = new Author(1, "Test", "Test Test");
        Long id = authorDao.save(expectedAuthor);
        expectedAuthor.setId(id);
        List<Author> authorList = authorDao.findAll();
        Assert.assertEquals(1, authorList.size());
        Assert.assertEquals(expectedAuthor, authorList.get(0));
    }
}
