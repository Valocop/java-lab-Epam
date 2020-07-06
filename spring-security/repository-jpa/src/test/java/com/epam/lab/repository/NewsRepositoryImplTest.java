package com.epam.lab.repository;

import com.epam.lab.configuration.SpringRepoConfig;
import com.epam.lab.exception.RepositoryException;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.specification.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.epam.lab.repository.TestUtil.*;
import static com.epam.lab.specification.NewsSearchSpecification.TAGS_NAME;
import static com.epam.lab.specification.NewsSortSpecification.AUTHOR_NAME;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringRepoConfig.class},
        loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("dev")
@Transactional
public class NewsRepositoryImplTest {
    @Resource
    private NewsRepository newsRepository;
    @Resource
    private AuthorRepository authorRepository;
    @Resource
    private TagRepository tagRepository;

    @Test
    public void shouldSaveNews() {
        News news = getTestNews();
        News savedNews = newsRepository.save(news);
        Optional<News> newsOptional = newsRepository.findById(savedNews.getId());
        Assert.assertTrue(newsOptional.isPresent());
        Assert.assertEquals(savedNews, newsOptional.get());
    }

    @Test
    public void shouldUpdateNews() {
        News news = getTestNews();
        News savedNews = newsRepository.save(news);
        savedNews.setFullText("Another Text");
        savedNews.setTitle("New Title");
        newsRepository.update(savedNews);
        Optional<News> newsOptional = newsRepository.findById(savedNews.getId());
        Assert.assertTrue(newsOptional.isPresent());
        Assert.assertEquals(savedNews, newsOptional.get());
    }

    @Test
    public void shouldDeleteNews() {
        News news = getTestNews();
        News savedNews = newsRepository.save(news);
        Optional<News> newsOptional = newsRepository.findById(savedNews.getId());
        Assert.assertTrue(newsOptional.isPresent());
        Assert.assertEquals(savedNews, newsOptional.get());
        newsRepository.delete(savedNews);
        Optional<News> optionalNews = newsRepository.findById(savedNews.getId());
        Assert.assertFalse(optionalNews.isPresent());
    }

    @Test
    public void shouldCountNews() {
        long exceptCount = 10;
        for (int i = 0; i < exceptCount; i++) {
            newsRepository.save(getTestNews());
        }
        long actualCount = newsRepository.count();
        Assert.assertEquals(exceptCount, actualCount);
    }

    @Test
    public void shouldCountNewsBySearchTagNameSpecification() {
        News newsOne = getTestNews();
        Author author = getTestAuthor();
        Set<Tag> tagSet = getTestTags();
        News newsTwo = getTestNews();
        News newsTree = getTestNews();
        newsOne.setAuthor(author);
        newsOne.setTags(tagSet);
        newsTwo.setAuthor(author);
        newsTwo.setTags(tagSet);
        newsTree.setAuthor(author);
        newsTree.setTags(tagSet);
        tagSet.forEach(tag -> tagRepository.save(tag));
        authorRepository.save(author);
        newsRepository.save(newsOne);
        newsRepository.save(newsTwo);
        newsRepository.save(newsTree);
        NewsSearchSpecification specification =
                new NewsSearchSpecification(new SearchCriteria(TAGS_NAME, tagSet.iterator().next().getName()));
        long count = newsRepository.count(specification);
        Assert.assertEquals(3, count);
    }

    @Test
    public void shouldCountNewsBySearchTagNameAndAuthorNameSpecification() {
        News newsOne = getTestNews();
        News newsTwo = getTestNews();
        News newsThree = getTestNews();
        Author authorOne = getTestAuthor();
        Author authorTwo = getTestAuthor();
        Author authorThree = getTestAuthor();
        Tag tagOne = getTestTag();
        Tag tagTwo = getTestTag();
        newsOne.setAuthor(authorOne);
        newsOne.setTags(Collections.singleton(tagOne));
        newsTwo.setAuthor(authorTwo);
        newsTwo.setTags(Collections.singleton(tagTwo));
        newsThree.setAuthor(authorThree);
        authorRepository.save(authorOne);
        authorRepository.save(authorTwo);
        authorRepository.save(authorThree);
        tagRepository.save(tagOne);
        tagRepository.save(tagTwo);
        newsRepository.save(newsOne);
        newsRepository.save(newsTwo);
        newsRepository.save(newsThree);
        SearchCriteria tagSearchCriteria = new SearchCriteria(TAGS_NAME, tagOne.getName());
        SearchCriteria authorSearchCriteria = new SearchCriteria(AUTHOR_NAME, authorOne.getName());
        NewsSpecificationBuilder specificationBuilder = new NewsSpecificationBuilder();
        SearchSpecification<News> newsSearchSpecification = specificationBuilder
                .with(tagSearchCriteria)
                .with(authorSearchCriteria)
                .build();
        long actualResult = newsRepository.count(newsSearchSpecification);
        Assert.assertEquals(1, actualResult);
    }

    @Test
    public void shouldSearchNewsByAuthorName() {
        News newsOne = getTestNews();
        Author authorOne = getTestAuthor();
        Set<Tag> tagSetOne = getTestTags();
        News newsTwo = getTestNews();
        Author authorTwo = getTestAuthor();
        Set<Tag> tagSetTwo = getTestTags();
        newsOne.setTags(tagSetOne);
        newsTwo.setTags(tagSetTwo);
        newsOne.setAuthor(authorOne);
        newsTwo.setAuthor(authorTwo);
        authorRepository.save(authorOne);
        authorRepository.save(authorTwo);
        tagSetOne.forEach(tag -> tagRepository.save(tag));
        tagSetTwo.forEach(tag -> tagRepository.save(tag));
        News savedNewsOne = newsRepository.save(newsOne);
        newsRepository.save(newsTwo);
        NewsSearchSpecification specification =
                new NewsSearchSpecification(new SearchCriteria(NewsSearchSpecification.AUTHOR_NAME, authorOne.getName()));
        List<News> newsList = newsRepository.findAll(specification);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(1, newsList.size());
        Assert.assertEquals(savedNewsOne, newsList.get(0));
    }

    @Test
    public void shouldSearchNewsByTagName() {
        News newsOne = getTestNews();
        Author authorOne = getTestAuthor();
        Set<Tag> tagSetOne = getTestTags();
        News newsTwo = getTestNews();
        Author authorTwo = getTestAuthor();
        Set<Tag> tagSetTwo = getTestTags();
        newsOne.setTags(tagSetOne);
        newsTwo.setTags(tagSetTwo);
        newsOne.setAuthor(authorOne);
        newsTwo.setAuthor(authorTwo);
        authorRepository.save(authorOne);
        authorRepository.save(authorTwo);
        tagSetOne.forEach(tag -> tagRepository.save(tag));
        tagSetTwo.forEach(tag -> tagRepository.save(tag));
        News savedNewsOne = newsRepository.save(newsOne);
        newsRepository.save(newsTwo);
        String tagName = tagSetOne.iterator().next().getName();
        NewsSearchSpecification specification =
                new NewsSearchSpecification(new SearchCriteria("tags_name", tagName));
        List<News> newsList = newsRepository.findAll(specification);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(1, newsList.size());
        Assert.assertEquals(savedNewsOne, newsList.get(0));
    }

    @Test
    public void shouldSearchSeveralNewsByTagName() {
        News newsOne = getTestNews();
        Author authorOne = getTestAuthor();
        Set<Tag> tagSetOne = getTestTags();
        News newsTwo = getTestNews();
        Author authorTwo = getTestAuthor();
        newsOne.setTags(tagSetOne);
        newsTwo.setTags(tagSetOne);
        newsOne.setAuthor(authorOne);
        newsTwo.setAuthor(authorTwo);
        authorRepository.save(authorOne);
        authorRepository.save(authorTwo);
        tagSetOne.forEach(tag -> tagRepository.save(tag));
        newsRepository.save(newsOne);
        newsRepository.save(newsTwo);
        String tagName = tagSetOne.iterator().next().getName();
        NewsSearchSpecification specification =
                new NewsSearchSpecification(new SearchCriteria("tags_name", tagName));
        List<News> newsList = newsRepository.findAll(specification);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(2, newsList.size());
    }

    @Test
    public void shouldSearchAllNewsWithSort() {
        News newsOne = getTestNews();
        News newsTwo = getTestNews();
        News newsThree = getTestNews();
        Author authorOne = getTestAuthor();
        Author authorTwo = getTestAuthor();
        Author authorThree = getTestAuthor();
        Set<Tag> tagSetOne = getTestTags();
        Set<Tag> tagSetTwo = getTestTags();
        Set<Tag> tagSetThree = getTestTags();
        newsOne.setTags(tagSetOne);
        newsTwo.setTags(tagSetTwo);
        newsThree.setTags(tagSetThree);
        newsOne.setAuthor(authorOne);
        newsTwo.setAuthor(authorTwo);
        newsThree.setAuthor(authorThree);
        authorRepository.save(authorOne);
        authorRepository.save(authorTwo);
        authorRepository.save(authorThree);
        tagSetOne.forEach(tag -> tagRepository.save(tag));
        tagSetTwo.forEach(tag -> tagRepository.save(tag));
        tagSetThree.forEach(tag -> tagRepository.save(tag));
        newsRepository.save(newsOne);
        newsRepository.save(newsTwo);
        newsRepository.save(newsThree);
        List<News> sortedTestList = Arrays.asList(newsOne, newsTwo, newsThree);
        sortedTestList.sort(Comparator.comparing(o -> o.getAuthor().getName()));
        NewsSortSpecification sortSpecification = new NewsSortSpecification(new SortCriteria(AUTHOR_NAME));
        List<News> newsRepositoryList = newsRepository.findAll(sortSpecification, 10, 1);
        Assert.assertEquals(sortedTestList, newsRepositoryList);
    }

    @Test
    public void shouldSearchNewsByTagsNames() {
        News newsOne = getTestNews();
        Author authorOne = getTestAuthor();
        Set<Tag> tagSetOne = getTestTags();
        tagSetOne.addAll(getTestTags());
        News newsTwo = getTestNews();
        Author authorTwo = getTestAuthor();
        Set<Tag> tagSetTwo = getTestTags();
        newsOne.setTags(tagSetOne);
        newsTwo.setTags(tagSetTwo);
        newsOne.setAuthor(authorOne);
        newsTwo.setAuthor(authorTwo);
        authorRepository.save(authorOne);
        authorRepository.save(authorTwo);
        tagSetOne.forEach(tag -> tagRepository.save(tag));
        tagSetTwo.forEach(tag -> tagRepository.save(tag));
        News savedNewsOne = newsRepository.save(newsOne);
        newsRepository.save(newsTwo);
        List<String> tagsNames = tagSetOne.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        SearchCriteria criteriaOne = new SearchCriteria("tags_name", tagsNames.get(0));
        SearchCriteria criteriaTwo = new SearchCriteria("tags_name", tagsNames.get(1));
        SearchSpecification<News> newsSpecification = new NewsSpecificationBuilder()
                .with(criteriaOne)
                .with(criteriaTwo)
                .build();
        List<News> newsList = newsRepository.findAll(newsSpecification);
        Assert.assertNotNull(newsList);
        Assert.assertEquals(1, newsList.size());
        Assert.assertEquals(savedNewsOne, newsList.get(0));
    }

    @Test
    public void shouldDeleteAuthorWithNewsAndTags() {
        Author author = getTestAuthor();
        News news = getTestNews();
        Set<Tag> tags = getTestTags();
        news.setTags(tags);
        author.setNews(Collections.singletonList(news));
        Author savedAuthor = authorRepository.save(author);
        Assert.assertNotNull(savedAuthor);
        Optional<Author> optionalAuthor = authorRepository.findById(savedAuthor.getId());
        Assert.assertTrue(optionalAuthor.isPresent());
        authorRepository.delete(savedAuthor);
        Optional<Author> authorOptional = authorRepository.findById(savedAuthor.getId());
        Assert.assertFalse(authorOptional.isPresent());
        Optional<News> newsOptional = newsRepository.findById(news.getId());
        Assert.assertFalse(newsOptional.isPresent());
        tags.forEach(tag -> {
            Optional<Tag> optionalTag = tagRepository.findById(tag.getId());
            Assert.assertFalse(optionalTag.isPresent());
        });
    }

    @Test
    public void shouldDeleteNewsWithoutAuthorAndTags() {
        News news = getTestNews();
        Author author = getTestAuthor();
        Set<Tag> tagSet = getTestTags();
        tagSet.forEach(tag -> tagRepository.save(tag));
        Author savedAuthor = authorRepository.save(author);
        Assert.assertNotNull(savedAuthor);
        news.setTags(tagSet);
        news.setAuthor(author);
        News savedNews = newsRepository.save(news);
        Assert.assertNotNull(savedNews);
        Optional<News> newsOptional = newsRepository.findById(savedNews.getId());
        Assert.assertTrue(newsOptional.isPresent());
        Assert.assertEquals(savedNews, newsOptional.get());
        newsRepository.delete(news);
        Optional<News> optionalNews = newsRepository.findById(savedNews.getId());
        Assert.assertFalse(optionalNews.isPresent());
        Optional<Author> optionalAuthor = authorRepository.findById(author.getId());
        Assert.assertTrue(optionalAuthor.isPresent());
        tagSet.forEach(tag -> {
            Optional<Tag> optionalTag = tagRepository.findById(tag.getId());
            Assert.assertTrue(optionalTag.isPresent());
        });
    }

    @Test
    public void shouldSortNewsByAuthorName() {
        News newsOne = getTestNews();
        News newsTwo = getTestNews();
        News newsThree = getTestNews();
        Author authorOne = getTestAuthor();
        Author authorTwo = getTestAuthor();
        Author authorThree = getTestAuthor();
        Set<Tag> tagSet = getTestTags();
        newsOne.setTags(tagSet);
        newsTwo.setTags(tagSet);
        newsThree.setTags(tagSet);
        newsOne.setAuthor(authorOne);
        newsTwo.setAuthor(authorTwo);
        newsThree.setAuthor(authorThree);
        authorRepository.save(authorOne);
        authorRepository.save(authorTwo);
        authorRepository.save(authorThree);
        tagSet.forEach(tag -> tagRepository.save(tag));
        newsRepository.save(newsOne);
        newsRepository.save(newsTwo);
        newsRepository.save(newsThree);
        List<News> newsList = newsRepository.findAll(
                new NewsSearchSpecification(new SearchCriteria(TAGS_NAME, new ArrayList<>(tagSet).get(0).getName())),
                new NewsSortSpecification(new SortCriteria(AUTHOR_NAME)), 10, 1);
        List<News> expectedNews = new ArrayList<>();
        Collections.addAll(expectedNews, newsOne, newsTwo, newsThree);
        expectedNews.sort(Comparator.comparing(o -> o.getAuthor().getName()));
        Assert.assertArrayEquals(expectedNews.toArray(), newsList.toArray());
    }

    @Test(expected = RepositoryException.class)
    public void shouldThrowExceptionByIncorrectData() {
        SearchCriteria criteria = new SearchCriteria("key", "value");
        NewsSearchSpecification specification = new NewsSearchSpecification(criteria);
        News newsOne = getTestNews();
        Author authorOne = getTestAuthor();
        Set<Tag> tagSetOne = getTestTags();
        newsOne.setTags(tagSetOne);
        newsOne.setAuthor(authorOne);
        authorRepository.save(authorOne);
        tagSetOne.forEach(tag -> tagRepository.save(tag));
        News savedNewsOne = newsRepository.save(newsOne);
        List<News> newsList = newsRepository.findAll(specification);
    }
}
