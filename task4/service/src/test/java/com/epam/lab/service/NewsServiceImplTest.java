package com.epam.lab.service;

import com.epam.lab.configuration.SpringServiceConfigTest;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.model.News;
import com.epam.lab.repository.NewsRepository;
import com.epam.lab.specification.SearchCriteria;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.Optional;

import static com.epam.lab.service.TestUtil.convertToEntity;
import static com.epam.lab.service.TestUtil.getTestNewsDto;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@RunWith(JUnit4.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringServiceConfigTest.class},
        loader = AnnotationConfigContextLoader.class)
public class NewsServiceImplTest {
    @Mock
    private NewsRepository newsRepositoryMock;
    @Mock
    private AuthorService authorServiceMock;
    @Mock
    private TagService tagServiceMock;
    private NewsService newsService;
    @Mock
    private ModelMapper modelMapperMock;

    @Before
    public void setUp() {
        newsRepositoryMock = mock(NewsRepository.class);
        modelMapperMock = mock(ModelMapper.class);
        authorServiceMock = mock(AuthorService.class);
        tagServiceMock = mock(TagService.class);
        newsService = new NewsServiceImpl(newsRepositoryMock, authorServiceMock, tagServiceMock, modelMapperMock);
    }

    @Test
    public void shouldCreateNewsDto() {
        NewsDto newsDto = getTestNewsDto();
        News newsEntity = convertToEntity(newsDto);
        newsEntity.setId(1);
        newsDto.getAuthor().setId(1);
        newsDto.getTags().forEach(tagDto -> tagDto.setId(nextLong(1, Long.MAX_VALUE)));

        when(modelMapperMock.map(newsDto, News.class)).thenReturn(newsEntity);
        when(modelMapperMock.map(newsEntity, NewsDto.class)).thenReturn(newsDto);
        when(newsRepositoryMock.save(any())).thenReturn(newsEntity);
        when(authorServiceMock.create(newsDto.getAuthor())).thenReturn(newsDto.getAuthor());
        newsDto.getTags().forEach(tagDto -> when(tagServiceMock.create(tagDto)).thenReturn(tagDto));

        newsService.create(newsDto);

        ArgumentCaptor<News> argumentCaptor = ArgumentCaptor.forClass(News.class);
        verify(newsRepositoryMock, times(1)).save(argumentCaptor.capture());
        verify(authorServiceMock, times(1)).create(newsDto.getAuthor());
        newsDto.getTags().forEach(tagDto ->
                verify(tagServiceMock, times(1)).create(tagDto));
        verifyNoMoreInteractions(newsRepositoryMock);

        News news = argumentCaptor.getValue();

        assertTrue(news.getId() > 0);
        assertThat(news.getTitle(), is(newsDto.getTitle()));
        assertThat(news.getFullText(), is(newsDto.getFullText()));
    }

    @Test
    public void shouldRollbackTransactionCreateNews() {
        NewsDto newsDto = getTestNewsDto();
        News newsEntity = convertToEntity(newsDto);
        newsEntity.setId(1);
        newsDto.getAuthor().setId(1);
        newsDto.getTags().forEach(tagDto -> tagDto.setId(nextLong(1, Long.MAX_VALUE)));

        when(modelMapperMock.map(newsDto, News.class)).thenReturn(newsEntity);
        when(modelMapperMock.map(newsEntity, NewsDto.class)).thenReturn(newsDto);
        when(newsRepositoryMock.save(any())).thenThrow(RuntimeException.class);
        when(authorServiceMock.create(newsDto.getAuthor())).thenReturn(newsDto.getAuthor());
        newsDto.getTags().forEach(tagDto -> when(tagServiceMock.create(tagDto)).thenReturn(tagDto));

        try {
            newsService.create(newsDto);
        } catch (RuntimeException e) {
            ArgumentCaptor<News> argumentCaptor = ArgumentCaptor.forClass(News.class);
            verify(newsRepositoryMock, times(1)).save(argumentCaptor.capture());
            verify(authorServiceMock, times(1)).create(newsDto.getAuthor());
            newsDto.getTags().forEach(tagDto ->
                    verify(tagServiceMock, times(1)).create(tagDto));
            verifyNoMoreInteractions(newsRepositoryMock);

            News news = argumentCaptor.getValue();

            assertTrue(news.getId() > 0);
            assertThat(news.getTitle(), is(newsDto.getTitle()));
            assertThat(news.getFullText(), is(newsDto.getFullText()));
        }
    }

    @Test
    public void shouldReadNewsDto() {
        NewsDto newsDto = getTestNewsDto();
        newsDto.setId(nextLong(1, Long.MAX_VALUE));
        News newsEntity = convertToEntity(newsDto);

        when(modelMapperMock.map(newsDto, News.class)).thenReturn(newsEntity);
        when(modelMapperMock.map(newsEntity, NewsDto.class)).thenReturn(newsDto);
        when(newsRepositoryMock.findById(anyLong())).thenReturn(Optional.of(newsEntity));

        Optional<NewsDto> dtoOptional = newsService.read(newsDto);

        verify(newsRepositoryMock, times(1)).findById(anyLong());
        verifyNoMoreInteractions(newsRepositoryMock);

        assertTrue(dtoOptional.isPresent());
        assertEquals(newsDto, dtoOptional.get());
    }

    @Test
    public void shouldUpdateNewsDto() {
        NewsDto newsDto = getTestNewsDto();
        newsDto.setId(nextLong(1, Long.MAX_VALUE));
        News newsEntity = convertToEntity(newsDto);
        newsEntity.setTitle(random(10, true, false));

        when(modelMapperMock.map(newsDto, News.class)).thenReturn(newsEntity);
        when(modelMapperMock.map(newsEntity, NewsDto.class)).thenReturn(newsDto);
        when(newsRepositoryMock.update(newsEntity)).thenReturn(newsEntity);

        newsService.update(newsDto);

        ArgumentCaptor<News> argumentCaptor = ArgumentCaptor.forClass(News.class);
        verify(newsRepositoryMock, times(1)).update(argumentCaptor.capture());
        verifyNoMoreInteractions(newsRepositoryMock);

        News news = argumentCaptor.getValue();

        assertTrue(news.getId() > 0);
        assertThat(news.getTitle(), not(newsDto.getTitle()));
        assertThat(news.getFullText(), Matchers.is(newsDto.getFullText()));
    }

    @Test
    public void shouldDeleteNewsDto() {
        NewsDto newsDto = getTestNewsDto();
        newsDto.setId(nextLong(1, Long.MAX_VALUE));
        News newsEntity = convertToEntity(newsDto);

        when(modelMapperMock.map(newsDto, News.class)).thenReturn(newsEntity);
        when(modelMapperMock.map(newsEntity, NewsDto.class)).thenReturn(newsDto);
        when(newsRepositoryMock.findById(newsDto.getId())).thenReturn(Optional.of(newsEntity));

        newsService.delete(newsDto);

        verify(newsRepositoryMock, times(1)).findById(newsDto.getId());
        verify(newsRepositoryMock, times(1)).delete(newsEntity);
        verifyNoMoreInteractions(newsRepositoryMock);
    }

    @Test
    public void shouldFindNewsDtoBySpecification() {
        NewsDto newsDto = getTestNewsDto();
        newsDto.setId(nextLong(1, Long.MAX_VALUE));
        News newsEntity = convertToEntity(newsDto);

        List<String> authorsName = singletonList(newsDto.getAuthor().getName());
        List<String> tagsName = singletonList(random(5));
        List<String> sort = singletonList("author_name");

        SearchCriteria authorNameCriteria = new SearchCriteria("author_name", authorsName.get(0));
        SearchCriteria tagNameCriteria = new SearchCriteria("tags_name", tagsName.get(0));

        when(modelMapperMock.map(newsDto, News.class)).thenReturn(newsEntity);
        when(modelMapperMock.map(newsEntity, NewsDto.class)).thenReturn(newsDto);

        newsService.findBySpecification(authorsName, tagsName, sort);

        verify(newsRepositoryMock, times(1)).findAll(any(), any());
    }
}
