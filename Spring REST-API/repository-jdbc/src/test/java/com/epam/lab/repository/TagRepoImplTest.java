package com.epam.lab.repository;

import com.epam.lab.model.Tag;
import com.epam.lab.specification.FindTagByIdSpec;
import com.epam.lab.specification.FindTagByNameSpec;
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
public class TagRepoImplTest {
    private static final String DELETE_FROM_TAG = "delete from tag";
    private static final String CREATE_DB = "createDB.sql";
    private static EmbeddedDatabase embeddedDatabase;
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
        tagRepo = new TagRepoImpl(embeddedDatabase);
    }

    @After
    public void cleanDatabase() {
        jdbcTemplate.update(DELETE_FROM_TAG);
    }

    @Test
    public void shouldSaveTag() {
        Tag expectedTag = new Tag(1, "Test");
        Long id = tagRepo.save(expectedTag);
        expectedTag.setId(id);
        List<Tag> tagList = tagRepo.findAll();
        Assert.assertEquals(1, tagList.size());
        Assert.assertEquals(expectedTag, tagList.get(0));
    }

    @Test(expected = DataAccessException.class)
    public void shouldThrowExceptionBySaveTagWithNullFields() {
        Tag incorrectTag = new Tag(0, null);
        tagRepo.save(incorrectTag);
    }

    @Test(expected = DataAccessException.class)
    public void shouldThrowExceptionBySaveTagWithIncorrectFields() {
        Tag incorrectTag = new Tag(0, "Teeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeest");
        tagRepo.save(incorrectTag);
    }

    @Test
    public void shouldUpdateTag() {
        Tag primaryTag = new Tag(1, "Name");
        Tag expectedTag = new Tag(1, "Test");
        Long id = tagRepo.save(primaryTag);
        Assert.assertTrue(id > 0);
        expectedTag.setId(id);
        boolean isUpdated = tagRepo.update(expectedTag);
        Assert.assertTrue(isUpdated);
        List<Tag> tagList = tagRepo.findBy(new FindTagByIdSpec(id, "tag"));
        Assert.assertEquals(1, tagList.size());
        Assert.assertEquals(expectedTag, tagList.get(0));
    }

    @Test
    public void shouldReturnFalseByUpdateNotExistTag() {
        Tag notExistTag = new Tag(1, "Test");
        boolean isUpdated = tagRepo.update(notExistTag);
        Assert.assertFalse(isUpdated);
    }

    @Test
    public void shouldDeleteTag() {
        Tag testTag = new Tag(1, "Test");
        Long id = tagRepo.save(testTag);
        Assert.assertNotNull(id);
        testTag.setId(id);
        boolean isDeleted = tagRepo.delete(testTag);
        Assert.assertTrue(isDeleted);
        List<Tag> allTags = tagRepo.findAll();
        Assert.assertEquals(0, allTags.size());
    }

    @Test
    public void shouldReturnFalseByDeleteNotExistTag() {
        Tag testTag = new Tag(1, "Test");
        boolean isDeleted = tagRepo.delete(testTag);
        Assert.assertFalse(isDeleted);
    }

    @Test
    public void shouldGetTagById() {
        Tag expectedTag = new Tag(1, "Test");
        Long id = tagRepo.save(expectedTag);
        Assert.assertNotNull(id);
        expectedTag.setId(id);
        List<Tag> tagList = tagRepo.findBy(new FindTagByIdSpec(id, "tag"));
        Assert.assertEquals(1, tagList.size());
        Assert.assertEquals(expectedTag, tagList.get(0));
    }

    @Test
    public void shouldReturnEmptyListOfTagsByFindWithNullSpec() {
        List<Tag> tagList = tagRepo.findBy(null);
        Assert.assertNotNull(tagList);
        Assert.assertTrue(tagList.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListByGetNotExistTagById() {
        Tag expectedTag = new Tag(1, "Test");
        List<Tag> tagList = tagRepo.findBy(new FindTagByIdSpec(expectedTag.getId(), "tag"));
        Assert.assertNotNull(tagList);
        Assert.assertTrue(tagList.isEmpty());
    }

    @Test
    public void shouldFindAllTags() {
        Tag testTag = new Tag(1, "Test");
        Long idOne = tagRepo.save(testTag);
        Long idTwo = tagRepo.save(testTag);
        Assert.assertNotNull(idOne);
        Assert.assertNotNull(idTwo);
        List<Tag> allTags = tagRepo.findAll();
        Assert.assertNotNull(allTags);
        Assert.assertEquals(2, allTags.size());
    }

    @Test
    public void shouldReturnEmptyListByFindAllTags() {
        List<Tag> tagList = tagRepo.findAll();
        Assert.assertNotNull(tagList);
        Assert.assertTrue(tagList.isEmpty());
    }

    @Test
    public void shouldFindTagByName() {
        Tag testTagOne = new Tag(1, "Test1");
        Tag testTagTwo = new Tag(1, "Test2");
        testTagOne.setId(tagRepo.save(testTagOne));
        testTagTwo.setId(tagRepo.save(testTagTwo));
        List<Tag> tags = tagRepo.findBy(new FindTagByNameSpec("Test1"));
        Assert.assertNotNull(tags);
        Assert.assertEquals(1, tags.size());
        Assert.assertEquals(testTagOne, tags.get(0));
    }

    @Test
    public void shouldDeleteUnsignedTags() {
        Tag testTagOne = new Tag(1, "Test1");
        Tag testTagTwo = new Tag(1, "Test2");
        testTagOne.setId(tagRepo.save(testTagOne));
        testTagTwo.setId(tagRepo.save(testTagTwo));
        List<Tag> tagRepoAll = tagRepo.findAll();
        Assert.assertEquals(2, tagRepoAll.size());
        boolean unsignedTagsDeleted = tagRepo.deleteUnsignedTags();
        Assert.assertTrue(unsignedTagsDeleted);
        List<Tag> tagList = tagRepo.findAll();
        Assert.assertEquals(0, tagList.size());
    }
}
