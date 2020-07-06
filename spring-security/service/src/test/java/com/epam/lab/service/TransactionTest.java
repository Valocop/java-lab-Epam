package com.epam.lab.service;

import com.epam.lab.configuration.SpringRepoConfig;
import com.epam.lab.configuration.TestConfiguration;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.model.News;
import com.epam.lab.repository.AuthorRepository;
import com.epam.lab.repository.NewsRepository;
import com.epam.lab.repository.TagRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.epam.lab.service.TestUtil.getTestNewsDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringRepoConfig.class, TestConfiguration.class},
        loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("dev")
public class TransactionTest {
    @Autowired
    @Qualifier("mockNewsRepository")
    private NewsRepository newsRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    @Qualifier("testNewsService")
    private NewsService newsService;

    @Test
    public void shouldRollBackTransaction() {
        NewsDto testNewsDto = getTestNewsDto();

        doThrow(RuntimeException.class).when(newsRepository).save(any(News.class));

        long newsCountBeforeCreate = newsRepository.count();
        long authorCountBeforeCreate = authorRepository.readAll().size();
        long tagCountBeforeCreate = tagRepository.readAll().size();

        try {
            newsService.create(testNewsDto);
        } catch (Exception e) {

        }

        long newsCountAfterCreate = newsRepository.count();
        long authorCountAfterCreate = authorRepository.readAll().size();
        long tagCountAfterCreate = tagRepository.readAll().size();

        Assert.assertEquals(newsCountBeforeCreate, newsCountAfterCreate);
        Assert.assertEquals(authorCountBeforeCreate, authorCountAfterCreate);
        Assert.assertEquals(tagCountBeforeCreate, tagCountAfterCreate);
    }
}
