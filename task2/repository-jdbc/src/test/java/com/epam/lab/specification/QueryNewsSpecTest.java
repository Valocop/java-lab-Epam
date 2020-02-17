package com.epam.lab.specification;

import com.epam.lab.criteria.Criteria;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.*;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class QueryNewsSpecTest {
    private static final String DELETE_FROM_AUTHOR = "delete from author";
    private static final String DELETE_FROM_TAG = "delete from tag";
    private static final String DELETE_FROM_NEWS = "delete from news";
    private static final String DELETE_FROM_NEWS_AUTHOR = "delete from news_author";
    private static final String DELETE_FROM_NEWS_TAG = "delete from news_tag";
    private static final String CREATE_DB = "createDB.sql";
    private static EmbeddedDatabase embeddedDatabase;
    private static AuthorRepo authorRepo;
    private static TagRepo tagRepo;
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
        authorRepo = new AuthorRepoImpl(embeddedDatabase);
        tagRepo = new TagRepoImpl(embeddedDatabase);
        newsRepo = new NewsRepoImpl(embeddedDatabase);
    }

    @After
    public void cleanDatabase() {
        jdbcTemplate.update(DELETE_FROM_NEWS_AUTHOR);
        jdbcTemplate.update(DELETE_FROM_NEWS_TAG);
        jdbcTemplate.update(DELETE_FROM_NEWS);
        jdbcTemplate.update(DELETE_FROM_TAG);
        jdbcTemplate.update(DELETE_FROM_AUTHOR);
    }

    @Test
    public void shouldSearchNewsByAuthorName() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        Author authorOne = new Author(1, "Author1", "Author1");
        Author authorTwo = new Author(1, "Author2", "Author2");
        authorOne.setId(authorRepo.save(authorOne));
        authorTwo.setId(authorRepo.save(authorTwo));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTree, authorTwo);
        Criteria searchByAuthorName = new Criteria("author_name", Collections.singletonList("Author1"));
        FindSpecification searchByAuthorNameSpec = new FindNewsSpec().add(searchByAuthorName);
        List<News> newsListByAuthorName = newsRepo.findBy(searchByAuthorNameSpec);
        Assert.assertNotNull(newsListByAuthorName);
        Assert.assertEquals(2, newsListByAuthorName.size());
    }

    @Test
    public void shouldSearchNewsByAuthorSurname() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        Author authorOne = new Author(1, "Author1", "Author1");
        Author authorTwo = new Author(1, "Author2", "Author2");
        authorOne.setId(authorRepo.save(authorOne));
        authorTwo.setId(authorRepo.save(authorTwo));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTree, authorTwo);
        Criteria searchByAuthorName = new Criteria("author_surname", Collections.singletonList("Author1"));
        FindSpecification searchByAuthorNameSpec = new FindNewsSpec().add(searchByAuthorName);
        List<News> newsListByAuthorName = newsRepo.findBy(searchByAuthorNameSpec);
        Assert.assertNotNull(newsListByAuthorName);
        Assert.assertEquals(2, newsListByAuthorName.size());
    }

    @Test
    public void shouldSearchNewsByAuthorNameAndSurname() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        Author authorOne = new Author(1, "Author1", "Author1");
        Author authorTwo = new Author(1, "Author2", "Author2");
        authorOne.setId(authorRepo.save(authorOne));
        authorTwo.setId(authorRepo.save(authorTwo));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTree, authorTwo);
        Criteria searchByAuthorName = new Criteria("author_name", Collections.singletonList("Author1"));
        Criteria searchByAuthorSurname = new Criteria("author_surname", Collections.singletonList("Author1"));
        FindSpecification searchByAuthorNameSpec = new FindNewsSpec().add(searchByAuthorName).add(searchByAuthorSurname);
        List<News> newsListByAuthorName = newsRepo.findBy(searchByAuthorNameSpec);
        Assert.assertNotNull(newsListByAuthorName);
        Assert.assertEquals(2, newsListByAuthorName.size());
    }

    @Test
    public void shouldSearchNewsWithoutCriteria() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        Author authorOne = new Author(1, "Author1", "Author1");
        Author authorTwo = new Author(1, "Author2", "Author2");
        authorOne.setId(authorRepo.save(authorOne));
        authorTwo.setId(authorRepo.save(authorTwo));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTree, authorTwo);
        FindSpecification searchByAuthorNameSpec = new FindNewsSpec();
        List<News> newsListByAuthorName = newsRepo.findBy(searchByAuthorNameSpec);
        Assert.assertNotNull(newsListByAuthorName);
        Assert.assertEquals(3, newsListByAuthorName.size());
    }

    @Test
    public void shouldSortNewsByDateDescWithAuthorSearch() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        Author authorOne = new Author(1, "Author1", "Author1");
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTree, authorOne);
        Criteria searchByAuthorName = new Criteria("author_surname", Collections.singletonList("Author1"));
        Criteria sortByCreationDate = new Criteria("date", true, Collections.singletonList("DESC"));
        FindSpecification searchByAuthorNameSpec = new FindNewsSpec().add(searchByAuthorName).add(sortByCreationDate);
        List<News> newsListByAuthorName = newsRepo.findBy(searchByAuthorNameSpec);
        Assert.assertNotNull(newsListByAuthorName);
        Assert.assertEquals(3, newsListByAuthorName.size());
        Assert.assertEquals(testNewsTwo, newsListByAuthorName.get(0));
        Assert.assertEquals(testNewsOne, newsListByAuthorName.get(1));
        Assert.assertEquals(testNewsTree, newsListByAuthorName.get(2));
    }

    @Test
    public void shouldSortNewsByDateAscWithAuthorSearch() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        Author authorOne = new Author(1, "Author1", "Author1");
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTree, authorOne);
        Criteria searchByAuthorName = new Criteria("author_name", Collections.singletonList("Author1"));
        Criteria sortByCreationDate = new Criteria("date", true, Collections.singletonList("ASC"));
        FindSpecification searchByAuthorNameSpec = new FindNewsSpec().add(searchByAuthorName).add(sortByCreationDate);
        List<News> newsListByAuthorName = newsRepo.findBy(searchByAuthorNameSpec);
        Assert.assertNotNull(newsListByAuthorName);
        Assert.assertEquals(3, newsListByAuthorName.size());
        Assert.assertEquals(testNewsTwo, newsListByAuthorName.get(2));
        Assert.assertEquals(testNewsOne, newsListByAuthorName.get(1));
        Assert.assertEquals(testNewsTree, newsListByAuthorName.get(0));
    }

    @Test
    public void shouldSortNewsAscByAuthorName() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        Author authorOne = new Author(1, "A", "A");
        Author authorTwo = new Author(2, "B", "B");
        Author authorThree = new Author(3, "C", "C");
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorTwo);
        newsRepo.createBindingOfAuthorAndNews(testNewsTree, authorThree);
        Criteria sortByAuthorNameCriteria = new Criteria("author_name", true, Collections.singletonList("ASC"));
        FindSpecification sortByAuthorNameSpec = new FindNewsSpec().add(sortByAuthorNameCriteria);
        List<News> newsList = newsRepo.findBy(sortByAuthorNameSpec);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(3, newsList.size());
        Assert.assertEquals(testNewsOne, newsList.get(0));
        Assert.assertEquals(testNewsTwo, newsList.get(1));
        Assert.assertEquals(testNewsTree, newsList.get(2));
    }

    @Test
    public void shouldSortNewsDescByAuthorName() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        Author authorOne = new Author(1, "A", "A");
        Author authorTwo = new Author(2, "B", "B");
        Author authorThree = new Author(3, "C", "C");
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorTwo);
        newsRepo.createBindingOfAuthorAndNews(testNewsTree, authorThree);
        Criteria sortByAuthorNameCriteria = new Criteria("author_name", true, Collections.singletonList("DESC"));
        FindSpecification sortByAuthorNameSpec = new FindNewsSpec().add(sortByAuthorNameCriteria);
        List<News> newsList = newsRepo.findBy(sortByAuthorNameSpec);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(3, newsList.size());
        Assert.assertEquals(testNewsOne, newsList.get(2));
        Assert.assertEquals(testNewsTwo, newsList.get(1));
        Assert.assertEquals(testNewsTree, newsList.get(0));
    }

    @Test
    public void shouldSortNewsAscByAuthorSurname() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        Author authorOne = new Author(1, "A", "Abc");
        Author authorTwo = new Author(2, "B", "Bdd");
        Author authorThree = new Author(3, "C", "Ccc");
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorTwo);
        newsRepo.createBindingOfAuthorAndNews(testNewsTree, authorThree);
        Criteria sortByAuthorNameCriteria = new Criteria("author_surname", true, Collections.singletonList("ASC"));
        FindSpecification sortByAuthorNameSpec = new FindNewsSpec().add(sortByAuthorNameCriteria);
        List<News> newsList = newsRepo.findBy(sortByAuthorNameSpec);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(3, newsList.size());
        Assert.assertEquals(testNewsOne, newsList.get(0));
        Assert.assertEquals(testNewsTwo, newsList.get(1));
        Assert.assertEquals(testNewsTree, newsList.get(2));
    }

    @Test
    public void shouldSortNewsDescByAuthorSurname() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        Author authorOne = new Author(1, "A", "Abc");
        Author authorTwo = new Author(2, "B", "Bdd");
        Author authorThree = new Author(3, "C", "Ccc");
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorTwo);
        newsRepo.createBindingOfAuthorAndNews(testNewsTree, authorThree);
        Criteria sortByAuthorNameCriteria = new Criteria("author_surname", true, Collections.singletonList("DESC"));
        FindSpecification sortByAuthorNameSpec = new FindNewsSpec().add(sortByAuthorNameCriteria);
        List<News> newsList = newsRepo.findBy(sortByAuthorNameSpec);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(3, newsList.size());
        Assert.assertEquals(testNewsOne, newsList.get(2));
        Assert.assertEquals(testNewsTwo, newsList.get(1));
        Assert.assertEquals(testNewsTree, newsList.get(0));
    }

    @Test
    public void shouldMultipleSortAscByAuthorNameAndSurname() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        Author authorOne = new Author(1, "A", "Abc");
        Author authorTwo = new Author(2, "A", "Zzz");
        Author authorThree = new Author(3, "A", "Ccc");
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorTwo);
        newsRepo.createBindingOfAuthorAndNews(testNewsTree, authorThree);
        Criteria sortByAuthorNameCriteria = new Criteria("author_name", true, Collections.singletonList("ASC"));
        Criteria sortByAuthorSurnameCriteria = new Criteria("author_surname", true, Collections.singletonList("ASC"));
        FindSpecification sortByAuthorNameSpec = new FindNewsSpec()
                .add(sortByAuthorSurnameCriteria)
                .add(sortByAuthorNameCriteria);
        List<News> newsList = newsRepo.findBy(sortByAuthorNameSpec);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(3, newsList.size());
        Assert.assertEquals(testNewsOne, newsList.get(0));
        Assert.assertEquals(testNewsTwo, newsList.get(2));
        Assert.assertEquals(testNewsTree, newsList.get(1));
    }

    @Test
    public void shouldMultipleSortCombByAuthorNameAndSurname() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        Author authorOne = new Author(1, "A", "Abc");
        Author authorTwo = new Author(2, "A", "Zzz");
        Author authorThree = new Author(3, "B", "Ccc");
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorTwo);
        newsRepo.createBindingOfAuthorAndNews(testNewsTree, authorThree);
        Criteria sortByAuthorNameCriteria = new Criteria("author_name", true, Collections.singletonList("ASC"));
        Criteria sortByAuthorSurnameCriteria = new Criteria("author_surname", true, Collections.singletonList("DESC"));
        FindSpecification sortByAuthorNameSpec = new FindNewsSpec()
                .add(sortByAuthorSurnameCriteria)
                .add(sortByAuthorNameCriteria);
        List<News> newsList = newsRepo.findBy(sortByAuthorNameSpec);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(3, newsList.size());
        Assert.assertEquals(testNewsOne, newsList.get(2));
        Assert.assertEquals(testNewsTwo, newsList.get(0));
        Assert.assertEquals(testNewsTree, newsList.get(1));
    }

    @Test
    public void shouldSearchNewsByTagName() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsThree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        Tag tagOne = new Tag(1, "Tag1");
        Tag tagTwo = new Tag(2, "Tag2");
        Tag tagThree = new Tag(3, "Tag3");
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsThree.setId(newsRepo.save(testNewsThree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        tagOne.setId(tagRepo.save(tagOne));
        tagTwo.setId(tagRepo.save(tagTwo));
        tagThree.setId(tagRepo.save(tagThree));
        newsRepo.createBindingOfNewsAndTags(testNewsOne, tagOne);
        newsRepo.createBindingOfNewsAndTags(testNewsTwo, tagTwo);
        newsRepo.createBindingOfNewsAndTags(testNewsThree, tagThree);
        Criteria searchNewsByTagOneCriteria = new Criteria("tag_name", Collections.singletonList("Tag1"));
        FindSpecification searchNewsByTagOneSpec = new FindNewsSpec().add(searchNewsByTagOneCriteria);
        List<News> searchListOne = newsRepo.findBy(searchNewsByTagOneSpec);
        Assert.assertEquals(1, searchListOne.size());
        Assert.assertEquals(testNewsOne, searchListOne.get(0));
        Criteria searchNewsByTagTwoCriteria = new Criteria("tag_name", Collections.singletonList("Tag2"));
        FindSpecification searchNewsByTagTwoSpec = new FindNewsSpec().add(searchNewsByTagTwoCriteria);
        List<News> searchListTwo = newsRepo.findBy(searchNewsByTagTwoSpec);
        Assert.assertEquals(1, searchListTwo.size());
        Assert.assertEquals(testNewsTwo, searchListTwo.get(0));
        Criteria searchNewsByTagThreeCriteria = new Criteria("tag_name", Collections.singletonList("Tag3"));
        FindSpecification searchNewsByThreeTwoSpec = new FindNewsSpec().add(searchNewsByTagThreeCriteria);
        List<News> searchListThree = newsRepo.findBy(searchNewsByThreeTwoSpec);
        Assert.assertEquals(1, searchListThree.size());
        Assert.assertEquals(testNewsThree, searchListThree.get(0));
    }

    @Test
    public void shouldSearchNewsByTags() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsThree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        Tag tagOne = new Tag(1, "Tag1");
        Tag tagTwo = new Tag(2, "Tag2");
        Tag tagThree = new Tag(3, "Tag3");
        Tag tagFour = new Tag(4, "Tag4");
        Tag tagFive = new Tag(5, "Tag5");
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsThree.setId(newsRepo.save(testNewsThree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        tagOne.setId(tagRepo.save(tagOne));
        tagTwo.setId(tagRepo.save(tagTwo));
        tagThree.setId(tagRepo.save(tagThree));
        tagFour.setId(tagRepo.save(tagFour));
        tagFive.setId(tagRepo.save(tagFive));
        newsRepo.createBindingOfNewsAndTags(testNewsOne, tagOne);
        newsRepo.createBindingOfNewsAndTags(testNewsTwo, tagTwo);
        newsRepo.createBindingOfNewsAndTags(testNewsThree, tagThree);
        newsRepo.createBindingOfNewsAndTags(testNewsOne, tagFour);
        newsRepo.createBindingOfNewsAndTags(testNewsOne, tagFive);
        newsRepo.createBindingOfNewsAndTags(testNewsTwo, tagOne);
        Criteria searchNewsByTagOneCriteria = new Criteria("tag_name", Arrays.asList("Tag1", "Tag4"));
        FindSpecification searchNewsByTagOneSpec = new FindNewsSpec().add(searchNewsByTagOneCriteria);
        Set<News> newsSet = new LinkedHashSet<>(newsRepo.findBy(searchNewsByTagOneSpec));
        List<News> searchListOne = new ArrayList<>(newsSet);
        Assert.assertEquals(1, searchListOne.size());
        Assert.assertEquals(testNewsOne, searchListOne.get(0));
        Criteria searchNewsByTagTwoCriteria = new Criteria("tag_name", Arrays.asList("Tag1", "Tag4", "Tag5"));
        FindSpecification searchNewsByTagTwoSpec = new FindNewsSpec().add(searchNewsByTagTwoCriteria);
        Set<News> newsSetOne = new LinkedHashSet<>(newsRepo.findBy(searchNewsByTagTwoSpec));
        List<News> searchListTwo = new ArrayList<>(newsSetOne);
        Assert.assertEquals(1, searchListTwo.size());
        Assert.assertEquals(testNewsOne, searchListTwo.get(0));
        Criteria searchNewsByTagThreeCriteria = new Criteria("tag_name", Arrays.asList("Tag1"));
        FindSpecification searchNewsByTagThreeSpec = new FindNewsSpec().add(searchNewsByTagThreeCriteria);
        Set<News> newsSetTwo = new LinkedHashSet<>(newsRepo.findBy(searchNewsByTagThreeSpec));
        List<News> searchListThree = new ArrayList<>(newsSetTwo);
        Assert.assertEquals(2, searchListThree.size());
        Assert.assertTrue(searchListThree.contains(testNewsOne));
        Assert.assertTrue(searchListThree.contains(testNewsTwo));
    }

    @Test
    public void shouldSortAscNewsWithTagsByAuthorName() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsThree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        Author authorOne = new Author(1, "Andrei", "Zdanovich");
        Author authorTwo = new Author(2, "Vasia", "Pupkin");
        Author authorThree = new Author(3, "Ivan", "Ivanov");
        Tag tagOne = new Tag(1, "Tag1");
        Tag tagTwo = new Tag(2, "Tag2");
        Tag tagThree = new Tag(3, "Tag3");
        Tag tagFour = new Tag(4, "Tag4");
        Tag tagFive = new Tag(5, "Tag5");
        authorOne.setId(authorRepo.save(authorOne));
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsThree.setId(newsRepo.save(testNewsThree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        tagOne.setId(tagRepo.save(tagOne));
        tagTwo.setId(tagRepo.save(tagTwo));
        tagThree.setId(tagRepo.save(tagThree));
        tagFour.setId(tagRepo.save(tagFour));
        tagFive.setId(tagRepo.save(tagFive));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorTwo);
        newsRepo.createBindingOfAuthorAndNews(testNewsThree, authorThree);
        newsRepo.createBindingOfNewsAndTags(testNewsOne, tagOne);
        newsRepo.createBindingOfNewsAndTags(testNewsTwo, tagTwo);
        newsRepo.createBindingOfNewsAndTags(testNewsThree, tagThree);
        newsRepo.createBindingOfNewsAndTags(testNewsOne, tagFour);
        newsRepo.createBindingOfNewsAndTags(testNewsOne, tagFive);
        newsRepo.createBindingOfNewsAndTags(testNewsTwo, tagOne);
        Criteria searchNewsByTagOneCriteria = new Criteria("author_name", true, Collections.singletonList("ASC"));
        FindSpecification searchNewsByTagOneSpec = new FindNewsSpec().add(searchNewsByTagOneCriteria);
        Set<News> newsSetOne = new LinkedHashSet<>(newsRepo.findBy(searchNewsByTagOneSpec));
        List<News> searchListOne = new ArrayList<>(newsSetOne);
        Assert.assertEquals(3, searchListOne.size());
        Assert.assertEquals(testNewsOne, searchListOne.get(0));
        Assert.assertEquals(testNewsThree, searchListOne.get(1));
        Assert.assertEquals(testNewsTwo, searchListOne.get(2));
    }

    @Test
    public void shouldSortAscNewsWithTagsByAuthorNameAndDate() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                LocalDate.parse("2019-11-11"), LocalDate.parse("2019-11-11"));
        News testNewsThree = new News(3, "Test3", "Test3", "Test2",
                LocalDate.parse("2019-09-09"), LocalDate.parse("2019-12-12"));
        Author authorOne = new Author(1, "Andrei", "Zdanovich");
        Author authorTwo = new Author(2, "Andrei", "Pupkin");
        Author authorThree = new Author(3, "Ivan", "Ivanov");
        Tag tagOne = new Tag(1, "Tag1");
        Tag tagTwo = new Tag(2, "Tag2");
        Tag tagThree = new Tag(3, "Tag3");
        Tag tagFour = new Tag(4, "Tag4");
        Tag tagFive = new Tag(5, "Tag5");
        authorOne.setId(authorRepo.save(authorOne));
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsThree.setId(newsRepo.save(testNewsThree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        tagOne.setId(tagRepo.save(tagOne));
        tagTwo.setId(tagRepo.save(tagTwo));
        tagThree.setId(tagRepo.save(tagThree));
        tagFour.setId(tagRepo.save(tagFour));
        tagFive.setId(tagRepo.save(tagFive));
        newsRepo.createBindingOfAuthorAndNews(testNewsOne, authorOne);
        newsRepo.createBindingOfAuthorAndNews(testNewsTwo, authorTwo);
        newsRepo.createBindingOfAuthorAndNews(testNewsThree, authorThree);
        newsRepo.createBindingOfNewsAndTags(testNewsOne, tagOne);
        newsRepo.createBindingOfNewsAndTags(testNewsTwo, tagTwo);
        newsRepo.createBindingOfNewsAndTags(testNewsThree, tagThree);
        newsRepo.createBindingOfNewsAndTags(testNewsOne, tagFour);
        newsRepo.createBindingOfNewsAndTags(testNewsOne, tagFive);
        newsRepo.createBindingOfNewsAndTags(testNewsTwo, tagOne);
        Criteria sortNewsByAuthorName = new Criteria("author_name", true, Collections.singletonList("ASC"));
        Criteria sortNewsByDate = new Criteria("date", true, Collections.singletonList("ASC"));
        FindSpecification searchNewsByTagOneSpec = new FindNewsSpec().add(sortNewsByAuthorName).add(sortNewsByDate);
        Set<News> searchSetOne = new LinkedHashSet<>(newsRepo.findBy(searchNewsByTagOneSpec));
        List<News> searchListOne = new ArrayList<>(searchSetOne);
        Assert.assertEquals(3, searchListOne.size());
        Assert.assertEquals(testNewsOne, searchListOne.get(0));
        Assert.assertEquals(testNewsThree, searchListOne.get(2));
        Assert.assertEquals(testNewsTwo, searchListOne.get(1));
    }
}
