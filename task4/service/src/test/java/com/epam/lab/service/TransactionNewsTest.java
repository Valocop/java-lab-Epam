package com.epam.lab.service;

import com.epam.lab.configuration.SpringRepoConfig;
import com.epam.lab.configuration.SpringServiceConfig;
import com.epam.lab.configuration.TestConfiguration;
import com.epam.lab.repository.NewsRepository;
import com.epam.lab.repository.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringRepoConfig.class, SpringServiceConfig.class, TestConfiguration.class})
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransactionNewsTest {
    @Autowired
    private NewsService newsService;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private TagRepository tagRepository;

    @Test
    public void shouldRollbackTransactionalCreateNewsMethod() {

    }
}
