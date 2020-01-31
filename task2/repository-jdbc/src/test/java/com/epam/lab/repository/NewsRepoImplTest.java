package com.epam.lab.repository;

import com.epam.lab.model.News;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@RunWith(JUnit4.class)
public class NewsRepoImplTest {
    private static final String DELETE_FROM_NEWS = "delete from news.news";
    private static final String CREATE_DB = "createDB.sql";
    private static EmbeddedDatabase embeddedDatabase;
    private static NewsRepo newsRepo;
    private static JdbcTemplate jdbcTemplate;

    @BeforeClass
    public static void initDatabase() throws IOException {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript(CREATE_DB)
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
    }

    @AfterClass
    public static void dropDatabase() throws IOException {
        embeddedDatabase.shutdown();
    }

    @Before
    public void init() {
        newsRepo = new NewsRepoImpl(embeddedDatabase);
    }

    @After
    public void cleanDatabase() {
        jdbcTemplate.update(DELETE_FROM_NEWS);
    }

    @Test
    public void shouldSaveNews() {
        News expectedNews = new News(1, "Test", "Test", "Test",
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()));
        Long saveId = newsRepo.save(expectedNews);
        Assert.assertNotNull(saveId);
        expectedNews.setId(saveId);
        List<News> newsList = newsRepo.find(new QueryNewsByIdSpec(saveId, "news.news"));
        Assert.assertNotNull(newsList);
        Assert.assertEquals(1, newsList.size());
        Assert.assertEquals(expectedNews, newsList.get(0));
        List<News> allNews = newsRepo.findAll();
        Assert.assertNotNull(allNews);
        Assert.assertEquals(1, allNews.size());
    }

    @Test(expected = DataAccessException.class)
    public void shouldThrowExceptionBySaveNewsWithNullFields() {
        News expectedNews = new News(1, null, null, "Test",
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()));
        newsRepo.save(expectedNews);
    }

    @Test(expected = DataAccessException.class)
    public void shouldThrowExceptionBySaveNewsWithIncorrectFields() {
        News expectedNews = new News(1, "Teeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeest",
                "Test", "Test", new Date(new java.util.Date().getTime()),
                new Date(new java.util.Date().getTime()));
        newsRepo.save(expectedNews);
    }

    @Test
    public void shouldUpdateNews() {
        News primaryNews = new News(1, "Test", "Test", "Test",
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()));
        News expectedNews = new News(1, "New Test", "New Test", "Test Test",
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()));
        Long save = newsRepo.save(primaryNews);
        Assert.assertNotNull(save);
        expectedNews.setId(save);
        boolean isUpdated = newsRepo.update(expectedNews);
        Assert.assertTrue(isUpdated);
        List<News> newsList = newsRepo.find(new QueryNewsByIdSpec(save, "news.news"));
        Assert.assertNotNull(newsList);
        Assert.assertEquals(1, newsList.size());
        Assert.assertEquals(expectedNews, newsList.get(0));
    }

    @Test
    public void shouldReturnFalseByUpdateNotExistNews() {
        News testNews = new News(1, "New Test", "New Test", "Test Test",
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()));
        boolean isUpdated = newsRepo.update(testNews);
        Assert.assertFalse(isUpdated);
    }

    @Test
    public void shouldDeleteNews() {
        News testNews = new News(1, "New Test", "New Test", "Test Test",
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()));
        Long save = newsRepo.save(testNews);
        Assert.assertNotNull(save);
        testNews.setId(save);
        List<News> newsList = newsRepo.find(new QueryNewsByIdSpec(save, "news.news"));
        Assert.assertNotNull(newsList);
        Assert.assertEquals(testNews, newsList.get(0));
        boolean isDeleted = newsRepo.delete(testNews);
        Assert.assertTrue(isDeleted);
        List<News> emptyList = newsRepo.find(new QueryNewsByIdSpec(save, "news.news"));
        Assert.assertNotNull(newsList);
        Assert.assertEquals(0, emptyList.size());
    }

    @Test
    public void shouldGetNewsById() {
        News expectedNews = new News(1, "Test", "Test", "Test",
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()));
        Long save = newsRepo.save(expectedNews);
        Assert.assertNotNull(save);
        expectedNews.setId(save);
        List<News> newsList = newsRepo.find(new QueryNewsByIdSpec(save, "news.news"));
        Assert.assertNotNull(newsList);
        Assert.assertEquals(1, newsList.size());
        Assert.assertEquals(expectedNews, newsList.get(0));
    }

    @Test
    public void shouldReturnEmptyListOfByFindWithNullSpec() {
        List<News> newsList = newsRepo.find(null);
        Assert.assertNotNull(newsList);
        Assert.assertTrue(newsList.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListByGetNotExistAuthorById() {
        News expectedNews = new News(1, "Test", "Test", "Test",
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()));
        List<News> newsList = newsRepo.find(new QueryNewsByIdSpec(expectedNews.getId(), "news.news"));
        Assert.assertNotNull(newsList);
        Assert.assertTrue(newsList.isEmpty());
    }

    @Test
    public void shouldFindAllNews() {
        News testNews = new News(1, "Test", "Test", "Test",
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()));
        Long saveOne = newsRepo.save(testNews);
        Assert.assertNotNull(saveOne);
        Long saveTwo = newsRepo.save(testNews);
        Assert.assertNotNull(saveTwo);
        List<News> newsAll = newsRepo.findAll();
        Assert.assertNotNull(newsAll);
        Assert.assertEquals(2, newsAll.size());
    }

    @Test
    public void shouldReturnEmptyListByFindAllNews() {
        List<News> newsAll = newsRepo.findAll();
        Assert.assertNotNull(newsAll);
        Assert.assertEquals(0, newsAll.size());
    }
}
