package com.epam.lab.repository;

import com.epam.lab.configuration.SpringRepoConfig;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.epam.lab.repository.TestUtil.*;

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
}
