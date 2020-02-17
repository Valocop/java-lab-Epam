package com.epam.lab.repository;

import com.epam.lab.model.Author;
import com.epam.lab.specification.FindAuthorByIdAndNameSpec;
import com.epam.lab.specification.FindAuthorByIdSpec;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.io.IOException;
import java.util.List;

@RunWith(JUnit4.class)
public class AuthorRepoImplTest {
    private static final String DELETE_FROM_AUTHOR = "delete from author";
    private static final String CREATE_DB = "createDB.sql";
    private static EmbeddedDatabase embeddedDatabase;
    private static AuthorRepo authorRepo;
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
    }

    @After
    public void cleanDatabase() {
        jdbcTemplate.update(DELETE_FROM_AUTHOR);
    }

    @Test
    public void shouldSaveAuthor() {
        Author expectedAuthor = new Author(1, "Test", "Test Test");
        Long id = authorRepo.save(expectedAuthor);
        expectedAuthor.setId(id);
        List<Author> authorList = authorRepo.findAll();
        Assert.assertEquals(1, authorList.size());
        Assert.assertEquals(expectedAuthor, authorList.get(0));
    }

    @Test(expected = DataAccessException.class)
    public void shouldThrowExceptionBySaveAuthorWithNullFields() {
        Author incorrectAuthor = new Author(0, null, null);
        authorRepo.save(incorrectAuthor);
    }

    @Test(expected = DataAccessException.class)
    public void shouldThrowExceptionBySaveAuthorWithIncorrectFields() {
        Author incorrectAuthor = new Author(0, "looooooooooooooooooooooooooooong", "Test");
        authorRepo.save(incorrectAuthor);
    }

    @Test
    public void shouldUpdateAuthor() {
        Author primaryAuthor = new Author(1, "Name", "FullName");
        Author expectedAuthor = new Author(1, "Test", "Test Test");
        Long id = authorRepo.save(primaryAuthor);
        Assert.assertTrue(id > 0);
        expectedAuthor.setId(id);
        boolean isUpdated = authorRepo.update(expectedAuthor);
        Assert.assertTrue(isUpdated);
        List<Author> authorList = authorRepo.findBy(new FindAuthorByIdSpec(id, "author"));
        Assert.assertEquals(1, authorList.size());
        Assert.assertEquals(expectedAuthor, authorList.get(0));
    }

    @Test
    public void shouldReturnFalseByUpdateNotExistAuthor() {
        Author notExistAuthor = new Author(1, "Test", "Test test");
        boolean isUpdated = authorRepo.update(notExistAuthor);
        Assert.assertFalse(isUpdated);
    }

    @Test
    public void shouldDeleteAuthor() {
        Author testAuthor = new Author(1, "Test", "Test Test");
        Long id = authorRepo.save(testAuthor);
        Assert.assertNotNull(id);
        testAuthor.setId(id);
        boolean isDeleted = authorRepo.delete(testAuthor);
        Assert.assertTrue(isDeleted);
        List<Author> allAuthors = authorRepo.findAll();
        Assert.assertEquals(0, allAuthors.size());
    }

    @Test
    public void shouldReturnFalseByDeleteNotExistAuthor() {
        Author testAuthor = new Author(1, "Test", "Test Test");
        boolean isDeleted = authorRepo.delete(testAuthor);
        Assert.assertFalse(isDeleted);
    }

    @Test
    public void shouldGetAuthorById() {
        Author expectedAuthor = new Author(1, "Test", "Test Test");
        Long id = authorRepo.save(expectedAuthor);
        Assert.assertNotNull(id);
        expectedAuthor.setId(id);
        List<Author> authorList = authorRepo.findBy(new FindAuthorByIdSpec(id, "author"));
        Assert.assertEquals(1, authorList.size());
        Assert.assertEquals(expectedAuthor, authorList.get(0));
    }

    @Test
    public void shouldReturnEmptyListOfAuthorsByFindWithNullSpec() {
        List<Author> authorList = authorRepo.findBy(null);
        Assert.assertNotNull(authorList);
        Assert.assertTrue(authorList.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListByGetNotExistAuthorById() {
        Author expectedAuthor = new Author(1, "Test", "Test Test");
        List<Author> authorList = authorRepo.findBy(new FindAuthorByIdSpec(expectedAuthor.getId(), "author"));
        Assert.assertNotNull(authorList);
        Assert.assertTrue(authorList.isEmpty());
    }

    @Test
    public void shouldFindAllAuthors() {
        Author testAuthor = new Author(1, "Test", "Test Test");
        Long idOne = authorRepo.save(testAuthor);
        Long idTwo = authorRepo.save(testAuthor);
        Assert.assertNotNull(idOne);
        Assert.assertNotNull(idTwo);
        List<Author> allAuthors = authorRepo.findAll();
        Assert.assertNotNull(allAuthors);
        Assert.assertEquals(2, allAuthors.size());
    }

    @Test
    public void shouldReturnEmptyListByFindAllAuthors() {
        List<Author> authorList = authorRepo.findAll();
        Assert.assertNotNull(authorList);
        Assert.assertTrue(authorList.isEmpty());
    }

    @Test
    public void shouldFindAuthorVyIdAndName() {
        Author testAuthorOne = new Author(1, "Test1", "Test1 Test");
        Author testAuthorTwo = new Author(1, "Test2", "Test2 Test");
        testAuthorOne.setId(authorRepo.save(testAuthorOne));
        testAuthorTwo.setId(authorRepo.save(testAuthorTwo));
        List<Author> authors = authorRepo.findBy(new FindAuthorByIdAndNameSpec(testAuthorOne.getId(),
                testAuthorOne.getName()));
        Assert.assertNotNull(authors);
        Assert.assertEquals(1, authors.size());
        Assert.assertEquals(testAuthorOne, authors.get(0));
    }
}
