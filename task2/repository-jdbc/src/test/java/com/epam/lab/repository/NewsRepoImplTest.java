package com.epam.lab.repository;

import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.specification.*;
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
    private static final String DELETE_FROM_NEWS = "delete from news";
    private static final String DELETE_FROM_NEWS_AUTHOR = "delete from news_author";
    private static final String DELETE_FROM_AUTHOR = "delete from author";
    private static final String DELETE_FROM_TAG = "delete from tag";
    private static final String DELETE_FROM_NEWS_TAG = "delete from news_tag";
    private static final String CREATE_DB = "createDB.sql";
    private static EmbeddedDatabase embeddedDatabase;
    private static NewsRepo newsRepo;
    private static AuthorRepo authorRepo;
    private static TagRepo tagRepo;
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
        authorRepo = new AuthorRepoImpl(embeddedDatabase);
        tagRepo = new TagRepoImpl(embeddedDatabase);
    }

    @After
    public void cleanDatabase() {
        jdbcTemplate.update(DELETE_FROM_NEWS_AUTHOR);
        jdbcTemplate.update(DELETE_FROM_NEWS_TAG);
        jdbcTemplate.update(DELETE_FROM_AUTHOR);
        jdbcTemplate.update(DELETE_FROM_NEWS);
        jdbcTemplate.update(DELETE_FROM_TAG);
    }

    @Test
    public void shouldSaveNews() {
        News expectedNews = new News(1, "Test", "Test", "Test",
                new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime()));
        Long saveId = newsRepo.save(expectedNews);
        Assert.assertNotNull(saveId);
        expectedNews.setId(saveId);
        List<News> newsList = newsRepo.find(new QueryNewsByIdSpec(saveId, "news"));
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
        List<News> newsList = newsRepo.find(new QueryNewsByIdSpec(save, "news"));
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
        List<News> newsList = newsRepo.find(new QueryNewsByIdSpec(save, "news"));
        Assert.assertNotNull(newsList);
        Assert.assertEquals(testNews, newsList.get(0));
        boolean isDeleted = newsRepo.delete(testNews);
        Assert.assertTrue(isDeleted);
        List<News> emptyList = newsRepo.find(new QueryNewsByIdSpec(save, "news"));
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
        List<News> newsList = newsRepo.find(new QueryNewsByIdSpec(save, "news"));
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

    @Test
    public void shouldCreateAuthorToNews() {
        Author author = new Author(1, "Author", "Test Author");
        News news = new News(1, "Test", "Test", "Test", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        News newsTwo = new News(1, "Test2", "Test2", "Test", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        author.setId(authorRepo.save(author));
        news.setId(newsRepo.save(news));
        newsTwo.setId(newsRepo.save(newsTwo));
        boolean isCreated = newsRepo.createAuthorToNews(news, author);
        Assert.assertTrue(isCreated);
        List<News> newsList = newsRepo.find(new QueryNewsByAuthorIdSpec(author.getId()));
        Assert.assertNotNull(newsList);
        Assert.assertEquals(1, newsList.size());
        Assert.assertEquals(news, newsList.get(0));
    }

    @Test
    public void shouldCreateSeveralNewsByAuthor() {
        Author author = new Author(1, "Author", "Test Author");
        News news = new News(1, "Test", "Test", "Test", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        News newsTwo = new News(1, "Test2", "Test2", "Test", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        author.setId(authorRepo.save(author));
        news.setId(newsRepo.save(news));
        newsTwo.setId(newsRepo.save(newsTwo));
        boolean isCreatedOne = newsRepo.createAuthorToNews(news, author);
        Assert.assertTrue(isCreatedOne);
        boolean isCreatedTwo = newsRepo.createAuthorToNews(newsTwo, author);
        List<News> newsList = newsRepo.find(new QueryNewsByAuthorIdSpec(author.getId()));
        Assert.assertNotNull(newsList);
        Assert.assertEquals(2, newsList.size());
    }

    @Test
    public void shouldCreateNotExistAuthorToNews() {
        Author author = new Author(1, "Author", "Test Author");
        News news = new News(1, "Test", "Test", "Test", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        news.setId(newsRepo.save(news));
        boolean isCreated = newsRepo.createAuthorToNews(news, author);
        Assert.assertFalse(isCreated);
    }

    @Test
    public void shouldCreateNotExistNewsToAuthor() {
        Author author = new Author(1, "Author", "Test Author");
        News news = new News(1, "Test", "Test", "Test", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        author.setId(authorRepo.save(author));
        boolean isCreated = newsRepo.createAuthorToNews(news, author);
        Assert.assertFalse(isCreated);
    }

    @Test
    public void shouldDeleteAuthorOfNews() {
        Author author = new Author(1, "Author", "Test Author");
        News news = new News(1, "Test", "Test", "Test", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        author.setId(authorRepo.save(author));
        news.setId(newsRepo.save(news));
        boolean isCreated = newsRepo.createAuthorToNews(news, author);
        Assert.assertTrue(isCreated);
        boolean isDeleted = newsRepo.deleteAuthorOfNews(news);
        Assert.assertTrue(isDeleted);
        List<News> newsList = newsRepo.find(new QueryAuthorsByNewsIdSpec(news.getId()));
        Assert.assertNotNull(newsList);
        Assert.assertEquals(0, newsList.size());
    }

    @Test
    public void shouldDeleteAuthorOfNotExistNews() {
        Author author = new Author(1, "Author", "Test Author");
        News news = new News(1, "Test", "Test", "Test", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        author.setId(authorRepo.save(author));
        boolean isCreated = newsRepo.createAuthorToNews(news, author);
        Assert.assertFalse(isCreated);
        boolean isDeleted = newsRepo.deleteAuthorOfNews(news);
        Assert.assertFalse(isDeleted);
    }

    @Test
    public void shouldDeleteNewsOfAuthor() {
        Author author = new Author(1, "Author", "Test Author");
        News newsOne = new News(1, "Test", "Test", "Test", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        News newsTwo = new News(1, "Test2", "Test2", "Test2", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        author.setId(authorRepo.save(author));
        newsOne.setId(newsRepo.save(newsOne));
        newsTwo.setId(newsRepo.save(newsTwo));
        boolean isCreated = newsRepo.createAuthorToNews(newsOne, author);
        Assert.assertTrue(isCreated);
        boolean isCreatedTwo = newsRepo.createAuthorToNews(newsTwo, author);
        Assert.assertTrue(isCreatedTwo);
        boolean isDeleted = newsRepo.deleteNewsOfAuthor(author);
        Assert.assertTrue(isDeleted);
        List<News> newsList = newsRepo.find(new QueryNewsByAuthorIdSpec(author.getId()));
        Assert.assertNotNull(newsList);
        Assert.assertEquals(0, newsList.size());
    }

    @Test
    public void shouldDeleteNotExistNewsOfAuthor() {
        Author author = new Author(1, "Author", "Test Author");
        author.setId(authorRepo.save(author));
        boolean isDeleted = newsRepo.deleteNewsOfAuthor(author);
        Assert.assertFalse(isDeleted);
        List<News> newsList = newsRepo.find(new QueryNewsByAuthorIdSpec(author.getId()));
        Assert.assertNotNull(newsList);
        Assert.assertEquals(0, newsList.size());
    }

    @Test
    public void shouldDeleteNewsByNotExistAuthor() {
        Author author = new Author(1, "Author", "Test Author");
        News newsOne = new News(1, "Test", "Test", "Test", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        News newsTwo = new News(1, "Test2", "Test2", "Test2", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        newsOne.setId(newsRepo.save(newsOne));
        newsTwo.setId(newsRepo.save(newsTwo));
        boolean isDeleted = newsRepo.deleteNewsOfAuthor(author);
        Assert.assertFalse(isDeleted);
    }

    @Test
    public void shouldCreateTagToNews() {
        News newsOne = new News(1, "Test1", "Test1", "Test1", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        News newsTwo = new News(1, "Test2", "Test2", "Test2", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        Tag tag = new Tag(1, "TagTest1");
        newsOne.setId(newsRepo.save(newsOne));
        newsTwo.setId(newsRepo.save(newsTwo));
        tag.setId(tagRepo.save(tag));
        boolean isTagCreated = newsRepo.createTagToNews(newsOne, tag);
        Assert.assertTrue(isTagCreated);
        List<News> newsList = newsRepo.find(new QueryNewsByTagIdSpec(tag.getId()));
        Assert.assertNotNull(newsList);
        Assert.assertEquals(1, newsList.size());
        Assert.assertEquals(newsOne, newsList.get(0));
    }

    @Test
    public void shouldCreatedTagsToNews() {
        News newsOne = new News(1, "Test1", "Test1", "Test1", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        News newsTwo = new News(1, "Test2", "Test2", "Test2", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        Tag tagOne = new Tag(1, "TagTest1");
        Tag tagTwo = new Tag(2, "TagTest2");
        Tag tagThree = new Tag(3, "TagTest3");
        newsOne.setId(newsRepo.save(newsOne));
        newsTwo.setId(newsRepo.save(newsTwo));
        tagOne.setId(tagRepo.save(tagOne));
        tagTwo.setId(tagRepo.save(tagTwo));
        tagThree.setId(tagRepo.save(tagThree));
        boolean tagOneCreated = newsRepo.createTagToNews(newsOne, tagOne);
        boolean tagTwoCreated = newsRepo.createTagToNews(newsTwo, tagTwo);
        boolean tagThreeCreated = newsRepo.createTagToNews(newsTwo, tagThree);
        Assert.assertTrue(tagOneCreated);
        Assert.assertTrue(tagTwoCreated);
        Assert.assertTrue(tagThreeCreated);
        List<Tag> tagsListOne = tagRepo.find(new QueryTagsByNewsIdSpec(newsOne.getId()));
        Assert.assertNotNull(tagsListOne);
        Assert.assertEquals(1, tagsListOne.size());
        Assert.assertEquals(tagOne, tagsListOne.get(0));
        List<Tag> tagsListTwo = tagRepo.find(new QueryTagsByNewsIdSpec(newsTwo.getId()));
        Assert.assertNotNull(tagsListTwo);
        Assert.assertEquals(2, tagsListTwo.size());
        Assert.assertEquals(tagTwo, tagsListTwo.get(0));
        Assert.assertEquals(tagThree, tagsListTwo.get(1));
    }

    @Test
    public void shouldDeleteTagOfNews() {
        News newsOne = new News(1, "Test1", "Test1", "Test1", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        News newsTwo = new News(1, "Test2", "Test2", "Test2", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        Tag tagOne = new Tag(1, "TagTest1");
        Tag tagTwo = new Tag(2, "TagTest2");
        Tag tagThree = new Tag(3, "TagTest3");
        newsOne.setId(newsRepo.save(newsOne));
        newsTwo.setId(newsRepo.save(newsTwo));
        tagOne.setId(tagRepo.save(tagOne));
        tagTwo.setId(tagRepo.save(tagTwo));
        tagThree.setId(tagRepo.save(tagThree));
        newsRepo.createTagToNews(newsOne, tagOne);
        newsRepo.createTagToNews(newsTwo, tagTwo);
        newsRepo.createTagToNews(newsTwo, tagThree);
        List<Tag> tagsListBefore = tagRepo.find(new QueryTagsByNewsIdSpec(newsOne.getId()));
        Assert.assertEquals(1, tagsListBefore.size());
        boolean isDeleted = newsRepo.deleteTagOfNews(newsOne, tagOne);
        Assert.assertTrue(isDeleted);
        List<Tag> tagsListAfter = tagRepo.find(new QueryTagsByNewsIdSpec(newsOne.getId()));
        Assert.assertEquals(0, tagsListAfter.size());
        List<Tag> tagList = tagRepo.find(new QueryTagByIdSpec(tagOne.getId()));
        Assert.assertEquals(1, tagList.size());
        Assert.assertEquals(tagOne, tagList.get(0));
    }

    @Test
    public void shouldDeleteTagsOfNews() {
        News newsOne = new News(1, "Test1", "Test1", "Test1", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        News newsTwo = new News(1, "Test2", "Test2", "Test2", Date.valueOf("2019-01-01"),
                Date.valueOf("2019-01-01"));
        Tag tagOne = new Tag(1, "TagTest1");
        Tag tagTwo = new Tag(2, "TagTest2");
        Tag tagThree = new Tag(3, "TagTest3");
        newsOne.setId(newsRepo.save(newsOne));
        newsTwo.setId(newsRepo.save(newsTwo));
        tagOne.setId(tagRepo.save(tagOne));
        tagTwo.setId(tagRepo.save(tagTwo));
        tagThree.setId(tagRepo.save(tagThree));
        newsRepo.createTagToNews(newsOne, tagOne);
        newsRepo.createTagToNews(newsTwo, tagTwo);
        newsRepo.createTagToNews(newsTwo, tagThree);
        List<Tag> tagsListBefore = tagRepo.find(new QueryTagsByNewsIdSpec(newsTwo.getId()));
        Assert.assertEquals(2, tagsListBefore.size());
        boolean isTagsDeleted = newsRepo.deleteTagsOfNews(newsTwo);
        Assert.assertTrue(isTagsDeleted);
        List<Tag> tagsListAfter = tagRepo.find(new QueryTagsByNewsIdSpec(newsTwo.getId()));
        Assert.assertEquals(0, tagsListAfter.size());
        List<Tag> tagList = tagRepo.find(new QueryTagByIdSpec(tagTwo.getId()));
        Assert.assertEquals(1, tagList.size());
        Assert.assertEquals(tagTwo, tagList.get(0));
        List<Tag> tagListThree = tagRepo.find(new QueryTagByIdSpec(tagThree.getId()));
        Assert.assertEquals(1, tagListThree.size());
        Assert.assertEquals(tagThree, tagListThree.get(0));
    }
}
