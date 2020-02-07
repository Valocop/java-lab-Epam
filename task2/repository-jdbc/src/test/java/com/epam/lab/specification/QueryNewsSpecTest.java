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
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        Author authorOne = new Author(1, "Author1", "Author1");
        Author authorTwo = new Author(1, "Author2", "Author2");
        authorOne.setId(authorRepo.save(authorOne));
        authorTwo.setId(authorRepo.save(authorTwo));
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorOne);
        newsRepo.createAuthorToNews(testNewsTree, authorTwo);
        Criteria searchByAuthorName = new Criteria("author_name", Collections.singletonList("Author1"));
        QuerySpecification searchByAuthorNameSpec = new QueryNewsSpec().add(searchByAuthorName);
        List<News> newsListByAuthorName = newsRepo.find(searchByAuthorNameSpec);
        Assert.assertNotNull(newsListByAuthorName);
        Assert.assertEquals(2, newsListByAuthorName.size());
    }

    @Test
    public void shouldSearchNewsByAuthorSurname() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        Author authorOne = new Author(1, "Author1", "Author1");
        Author authorTwo = new Author(1, "Author2", "Author2");
        authorOne.setId(authorRepo.save(authorOne));
        authorTwo.setId(authorRepo.save(authorTwo));
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorOne);
        newsRepo.createAuthorToNews(testNewsTree, authorTwo);
        Criteria searchByAuthorName = new Criteria("author_surname", Collections.singletonList("Author1"));
        QuerySpecification searchByAuthorNameSpec = new QueryNewsSpec().add(searchByAuthorName);
        List<News> newsListByAuthorName = newsRepo.find(searchByAuthorNameSpec);
        Assert.assertNotNull(newsListByAuthorName);
        Assert.assertEquals(2, newsListByAuthorName.size());
    }

    @Test
    public void shouldSearchNewsByAuthorNameAndSurname() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        Author authorOne = new Author(1, "Author1", "Author1");
        Author authorTwo = new Author(1, "Author2", "Author2");
        authorOne.setId(authorRepo.save(authorOne));
        authorTwo.setId(authorRepo.save(authorTwo));
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorOne);
        newsRepo.createAuthorToNews(testNewsTree, authorTwo);
        Criteria searchByAuthorName = new Criteria("author_name", Collections.singletonList("Author1"));
        Criteria searchByAuthorSurname = new Criteria("author_surname", Collections.singletonList("Author1"));
        QuerySpecification searchByAuthorNameSpec = new QueryNewsSpec().add(searchByAuthorName).add(searchByAuthorSurname);
        List<News> newsListByAuthorName = newsRepo.find(searchByAuthorNameSpec);
        Assert.assertNotNull(newsListByAuthorName);
        Assert.assertEquals(2, newsListByAuthorName.size());
    }

    @Test
    public void shouldSearchNewsWithoutCriteria() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        Author authorOne = new Author(1, "Author1", "Author1");
        Author authorTwo = new Author(1, "Author2", "Author2");
        authorOne.setId(authorRepo.save(authorOne));
        authorTwo.setId(authorRepo.save(authorTwo));
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorOne);
        newsRepo.createAuthorToNews(testNewsTree, authorTwo);
        QuerySpecification searchByAuthorNameSpec = new QueryNewsSpec();
        List<News> newsListByAuthorName = newsRepo.find(searchByAuthorNameSpec);
        Assert.assertNotNull(newsListByAuthorName);
        Assert.assertEquals(3, newsListByAuthorName.size());
    }

    @Test
    public void shouldSortNewsByDateDescWithAuthorSearch() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        Author authorOne = new Author(1, "Author1", "Author1");
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorOne);
        newsRepo.createAuthorToNews(testNewsTree, authorOne);
        Criteria searchByAuthorName = new Criteria("author_surname", Collections.singletonList("Author1"));
        Criteria sortByCreationDate = new Criteria("date", true, Collections.singletonList("DESC"));
        QuerySpecification searchByAuthorNameSpec = new QueryNewsSpec().add(searchByAuthorName).add(sortByCreationDate);
        List<News> newsListByAuthorName = newsRepo.find(searchByAuthorNameSpec);
        Assert.assertNotNull(newsListByAuthorName);
        Assert.assertEquals(3, newsListByAuthorName.size());
        Assert.assertEquals(testNewsTwo, newsListByAuthorName.get(0));
        Assert.assertEquals(testNewsOne, newsListByAuthorName.get(1));
        Assert.assertEquals(testNewsTree, newsListByAuthorName.get(2));
    }

    @Test
    public void shouldSortNewsByDateAscWithAuthorSearch() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        Author authorOne = new Author(1, "Author1", "Author1");
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorOne);
        newsRepo.createAuthorToNews(testNewsTree, authorOne);
        Criteria searchByAuthorName = new Criteria("author_name", Collections.singletonList("Author1"));
        Criteria sortByCreationDate = new Criteria("date", true, Collections.singletonList("ASC"));
        QuerySpecification searchByAuthorNameSpec = new QueryNewsSpec().add(searchByAuthorName).add(sortByCreationDate);
        List<News> newsListByAuthorName = newsRepo.find(searchByAuthorNameSpec);
        Assert.assertNotNull(newsListByAuthorName);
        Assert.assertEquals(3, newsListByAuthorName.size());
        Assert.assertEquals(testNewsTwo, newsListByAuthorName.get(2));
        Assert.assertEquals(testNewsOne, newsListByAuthorName.get(1));
        Assert.assertEquals(testNewsTree, newsListByAuthorName.get(0));
    }

    @Test
    public void shouldSortNewsAscByAuthorName() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        Author authorOne = new Author(1, "A", "A");
        Author authorTwo = new Author(2, "B", "B");
        Author authorThree = new Author(3, "C", "C");
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorTwo);
        newsRepo.createAuthorToNews(testNewsTree, authorThree);
        Criteria sortByAuthorNameCriteria = new Criteria("author_name", true, Collections.singletonList("ASC"));
        QuerySpecification sortByAuthorNameSpec = new QueryNewsSpec().add(sortByAuthorNameCriteria);
        List<News> newsList = newsRepo.find(sortByAuthorNameSpec);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(3, newsList.size());
        Assert.assertEquals(testNewsOne, newsList.get(0));
        Assert.assertEquals(testNewsTwo, newsList.get(1));
        Assert.assertEquals(testNewsTree, newsList.get(2));
    }

    @Test
    public void shouldSortNewsDescByAuthorName() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        Author authorOne = new Author(1, "A", "A");
        Author authorTwo = new Author(2, "B", "B");
        Author authorThree = new Author(3, "C", "C");
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorTwo);
        newsRepo.createAuthorToNews(testNewsTree, authorThree);
        Criteria sortByAuthorNameCriteria = new Criteria("author_name", true, Collections.singletonList("DESC"));
        QuerySpecification sortByAuthorNameSpec = new QueryNewsSpec().add(sortByAuthorNameCriteria);
        List<News> newsList = newsRepo.find(sortByAuthorNameSpec);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(3, newsList.size());
        Assert.assertEquals(testNewsOne, newsList.get(2));
        Assert.assertEquals(testNewsTwo, newsList.get(1));
        Assert.assertEquals(testNewsTree, newsList.get(0));
    }

    @Test
    public void shouldSortNewsAscByAuthorSurname() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        Author authorOne = new Author(1, "A", "Abc");
        Author authorTwo = new Author(2, "B", "Bdd");
        Author authorThree = new Author(3, "C", "Ccc");
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorTwo);
        newsRepo.createAuthorToNews(testNewsTree, authorThree);
        Criteria sortByAuthorNameCriteria = new Criteria("author_surname", true, Collections.singletonList("ASC"));
        QuerySpecification sortByAuthorNameSpec = new QueryNewsSpec().add(sortByAuthorNameCriteria);
        List<News> newsList = newsRepo.find(sortByAuthorNameSpec);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(3, newsList.size());
        Assert.assertEquals(testNewsOne, newsList.get(0));
        Assert.assertEquals(testNewsTwo, newsList.get(1));
        Assert.assertEquals(testNewsTree, newsList.get(2));
    }

    @Test
    public void shouldSortNewsDescByAuthorSurname() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        Author authorOne = new Author(1, "A", "Abc");
        Author authorTwo = new Author(2, "B", "Bdd");
        Author authorThree = new Author(3, "C", "Ccc");
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorTwo);
        newsRepo.createAuthorToNews(testNewsTree, authorThree);
        Criteria sortByAuthorNameCriteria = new Criteria("author_surname", true, Collections.singletonList("DESC"));
        QuerySpecification sortByAuthorNameSpec = new QueryNewsSpec().add(sortByAuthorNameCriteria);
        List<News> newsList = newsRepo.find(sortByAuthorNameSpec);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(3, newsList.size());
        Assert.assertEquals(testNewsOne, newsList.get(2));
        Assert.assertEquals(testNewsTwo, newsList.get(1));
        Assert.assertEquals(testNewsTree, newsList.get(0));
    }

    @Test
    public void shouldMultipleSortAscByAuthorNameAndSurname() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        Author authorOne = new Author(1, "A", "Abc");
        Author authorTwo = new Author(2, "A", "Zzz");
        Author authorThree = new Author(3, "A", "Ccc");
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorTwo);
        newsRepo.createAuthorToNews(testNewsTree, authorThree);
        Criteria sortByAuthorNameCriteria = new Criteria("author_name", true, Collections.singletonList("ASC"));
        Criteria sortByAuthorSurnameCriteria = new Criteria("author_surname", true, Collections.singletonList("ASC"));
        QuerySpecification sortByAuthorNameSpec = new QueryNewsSpec()
                .add(sortByAuthorSurnameCriteria)
                .add(sortByAuthorNameCriteria);
        List<News> newsList = newsRepo.find(sortByAuthorNameSpec);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(3, newsList.size());
        Assert.assertEquals(testNewsOne, newsList.get(0));
        Assert.assertEquals(testNewsTwo, newsList.get(2));
        Assert.assertEquals(testNewsTree, newsList.get(1));
    }

    @Test
    public void shouldMultipleSortCombByAuthorNameAndSurname() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsTree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsTree.setId(newsRepo.save(testNewsTree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        Author authorOne = new Author(1, "A", "Abc");
        Author authorTwo = new Author(2, "A", "Zzz");
        Author authorThree = new Author(3, "B", "Ccc");
        authorTwo.setId(authorRepo.save(authorTwo));
        authorThree.setId(authorRepo.save(authorThree));
        authorOne.setId(authorRepo.save(authorOne));
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorTwo);
        newsRepo.createAuthorToNews(testNewsTree, authorThree);
        Criteria sortByAuthorNameCriteria = new Criteria("author_name", true, Collections.singletonList("ASC"));
        Criteria sortByAuthorSurnameCriteria = new Criteria("author_surname", true, Collections.singletonList("DESC"));
        QuerySpecification sortByAuthorNameSpec = new QueryNewsSpec()
                .add(sortByAuthorSurnameCriteria)
                .add(sortByAuthorNameCriteria);
        List<News> newsList = newsRepo.find(sortByAuthorNameSpec);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(3, newsList.size());
        Assert.assertEquals(testNewsOne, newsList.get(2));
        Assert.assertEquals(testNewsTwo, newsList.get(0));
        Assert.assertEquals(testNewsTree, newsList.get(1));
    }

    @Test
    public void shouldSearchNewsByTagName() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsThree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
        Tag tagOne = new Tag(1, "Tag1");
        Tag tagTwo = new Tag(2, "Tag2");
        Tag tagThree = new Tag(3, "Tag3");
        testNewsTwo.setId(newsRepo.save(testNewsTwo));
        testNewsThree.setId(newsRepo.save(testNewsThree));
        testNewsOne.setId(newsRepo.save(testNewsOne));
        tagOne.setId(tagRepo.save(tagOne));
        tagTwo.setId(tagRepo.save(tagTwo));
        tagThree.setId(tagRepo.save(tagThree));
        newsRepo.createTagToNews(testNewsOne, tagOne);
        newsRepo.createTagToNews(testNewsTwo, tagTwo);
        newsRepo.createTagToNews(testNewsThree, tagThree);
        Criteria searchNewsByTagOneCriteria = new Criteria("tag_name", Collections.singletonList("Tag1"));
        QuerySpecification searchNewsByTagOneSpec = new QueryNewsSpec().add(searchNewsByTagOneCriteria);
        List<News> searchListOne = newsRepo.find(searchNewsByTagOneSpec);
        Assert.assertEquals(1, searchListOne.size());
        Assert.assertEquals(testNewsOne, searchListOne.get(0));
        Criteria searchNewsByTagTwoCriteria = new Criteria("tag_name", Collections.singletonList("Tag2"));
        QuerySpecification searchNewsByTagTwoSpec = new QueryNewsSpec().add(searchNewsByTagTwoCriteria);
        List<News> searchListTwo = newsRepo.find(searchNewsByTagTwoSpec);
        Assert.assertEquals(1, searchListTwo.size());
        Assert.assertEquals(testNewsTwo, searchListTwo.get(0));
        Criteria searchNewsByTagThreeCriteria = new Criteria("tag_name", Collections.singletonList("Tag3"));
        QuerySpecification searchNewsByThreeTwoSpec = new QueryNewsSpec().add(searchNewsByTagThreeCriteria);
        List<News> searchListThree = newsRepo.find(searchNewsByThreeTwoSpec);
        Assert.assertEquals(1, searchListThree.size());
        Assert.assertEquals(testNewsThree, searchListThree.get(0));
    }

    @Test
    public void shouldSearchNewsByTags() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsThree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
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
        newsRepo.createTagToNews(testNewsOne, tagOne);
        newsRepo.createTagToNews(testNewsTwo, tagTwo);
        newsRepo.createTagToNews(testNewsThree, tagThree);
        newsRepo.createTagToNews(testNewsOne, tagFour);
        newsRepo.createTagToNews(testNewsOne, tagFive);
        newsRepo.createTagToNews(testNewsTwo, tagOne);
        Criteria searchNewsByTagOneCriteria = new Criteria("tag_name", Arrays.asList("Tag1", "Tag4"));
        QuerySpecification searchNewsByTagOneSpec = new QueryNewsSpec().add(searchNewsByTagOneCriteria);
        List<News> searchListOne = newsRepo.find(searchNewsByTagOneSpec);
        Assert.assertEquals(1, searchListOne.size());
        Assert.assertEquals(testNewsOne, searchListOne.get(0));
        Criteria searchNewsByTagTwoCriteria = new Criteria("tag_name", Arrays.asList("Tag1", "Tag4", "Tag5"));
        QuerySpecification searchNewsByTagTwoSpec = new QueryNewsSpec().add(searchNewsByTagTwoCriteria);
        List<News> searchListTwo = newsRepo.find(searchNewsByTagTwoSpec);
        Assert.assertEquals(1, searchListTwo.size());
        Assert.assertEquals(testNewsOne, searchListTwo.get(0));
        Criteria searchNewsByTagThreeCriteria = new Criteria("tag_name", Arrays.asList("Tag1"));
        QuerySpecification searchNewsByTagThreeSpec = new QueryNewsSpec().add(searchNewsByTagThreeCriteria);
        List<News> searchListThree = newsRepo.find(searchNewsByTagThreeSpec);
        Assert.assertEquals(2, searchListThree.size());
        Assert.assertTrue(searchListThree.contains(testNewsOne));
        Assert.assertTrue(searchListThree.contains(testNewsTwo));
    }

    @Test
    public void shouldSortAscNewsWithTagsByAuthorName() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsThree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
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
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorTwo);
        newsRepo.createAuthorToNews(testNewsThree, authorThree);
        newsRepo.createTagToNews(testNewsOne, tagOne);
        newsRepo.createTagToNews(testNewsTwo, tagTwo);
        newsRepo.createTagToNews(testNewsThree, tagThree);
        newsRepo.createTagToNews(testNewsOne, tagFour);
        newsRepo.createTagToNews(testNewsOne, tagFive);
        newsRepo.createTagToNews(testNewsTwo, tagOne);
        Criteria searchNewsByTagOneCriteria = new Criteria("author_name", true , Collections.singletonList("ASC"));
        QuerySpecification searchNewsByTagOneSpec = new QueryNewsSpec().add(searchNewsByTagOneCriteria);
        List<News> searchListOne = newsRepo.find(searchNewsByTagOneSpec);
        Assert.assertEquals(3, searchListOne.size());
        Assert.assertEquals(testNewsOne, searchListOne.get(0));
        Assert.assertEquals(testNewsThree, searchListOne.get(1));
        Assert.assertEquals(testNewsTwo, searchListOne.get(2));
    }

    @Test
    public void shouldSortAscNewsWithTagsByAuthorNameAndDate() {
        News testNewsOne = new News(1, "Test1", "Test1", "Test1",
                Date.valueOf("2019-10-10"), Date.valueOf("2019-10-10"));
        News testNewsTwo = new News(2, "Test2", "Test2", "Test2",
                Date.valueOf("2019-11-11"), Date.valueOf("2019-11-11"));
        News testNewsThree = new News(3, "Test3", "Test3", "Test2",
                Date.valueOf("2019-09-09"), Date.valueOf("2019-12-12"));
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
        newsRepo.createAuthorToNews(testNewsOne, authorOne);
        newsRepo.createAuthorToNews(testNewsTwo, authorTwo);
        newsRepo.createAuthorToNews(testNewsThree, authorThree);
        newsRepo.createTagToNews(testNewsOne, tagOne);
        newsRepo.createTagToNews(testNewsTwo, tagTwo);
        newsRepo.createTagToNews(testNewsThree, tagThree);
        newsRepo.createTagToNews(testNewsOne, tagFour);
        newsRepo.createTagToNews(testNewsOne, tagFive);
        newsRepo.createTagToNews(testNewsTwo, tagOne);
        Criteria sortNewsByAuthorName = new Criteria("author_name", true , Collections.singletonList("ASC"));
        Criteria sortNewsByDate = new Criteria("date", true , Collections.singletonList("ASC"));
        QuerySpecification searchNewsByTagOneSpec = new QueryNewsSpec().add(sortNewsByAuthorName).add(sortNewsByDate);
        List<News> searchListOne = newsRepo.find(searchNewsByTagOneSpec);
        Assert.assertEquals(3, searchListOne.size());
        Assert.assertEquals(testNewsOne, searchListOne.get(0));
        Assert.assertEquals(testNewsThree, searchListOne.get(2));
        Assert.assertEquals(testNewsTwo, searchListOne.get(1));
    }
}
