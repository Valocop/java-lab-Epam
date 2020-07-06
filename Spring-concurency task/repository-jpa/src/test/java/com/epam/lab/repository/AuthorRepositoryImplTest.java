package com.epam.lab.repository;

import com.epam.lab.configuration.SpringRepoConfig;
import com.epam.lab.model.Author;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.lab.repository.TestUtil.getTestAuthor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringRepoConfig.class},
        loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("dev")
@Transactional
public class AuthorRepositoryImplTest {
    @Resource
    private AuthorRepository authorRepository;

    @Test
    public void shouldSaveAuthor() {
        Author author = getTestAuthor();
        Author savedAuthor = authorRepository.save(author);
        Assert.assertNotNull(savedAuthor);
        Optional<Author> optionalAuthor = authorRepository.findById(savedAuthor.getId());
        Assert.assertTrue(optionalAuthor.isPresent());
        Assert.assertEquals(author, optionalAuthor.get());
    }

    @Test
    public void shouldUpdateAuthor() {
        Author author = getTestAuthor();
        Author savedAuthor = authorRepository.save(author);
        Assert.assertNotNull(savedAuthor);
        savedAuthor.setName("Another name");
        savedAuthor.setSurname("Another surname");
        authorRepository.update(savedAuthor);
        Optional<Author> optionalAuthor = authorRepository.findById(savedAuthor.getId());
        Assert.assertTrue(optionalAuthor.isPresent());
        Assert.assertEquals(savedAuthor, optionalAuthor.get());
    }

    @Test
    public void shouldDeleteAuthor() {
        Author author = getTestAuthor();
        Author savedAuthor = authorRepository.save(author);
        Optional<Author> optionalAuthor = authorRepository.findById(savedAuthor.getId());
        Assert.assertTrue(optionalAuthor.isPresent());
        Assert.assertEquals(author, optionalAuthor.get());
        authorRepository.delete(savedAuthor);
        Optional<Author> authorOptional = authorRepository.findById(savedAuthor.getId());
        Assert.assertFalse(authorOptional.isPresent());
    }

    @Test
    public void readAllAuthors() {
        Author authorOne = getTestAuthor();
        Author authorTwo = getTestAuthor();
        Author authorThree = getTestAuthor();
        authorRepository.save(authorOne);
        authorRepository.save(authorTwo);
        authorRepository.save(authorThree);
        List<Author> authorListTest = Arrays.asList(authorOne, authorTwo, authorThree);
        List<Author> authorList = authorRepository.readAll();
        Assert.assertEquals(authorListTest, authorList);
    }
}
