package com.epam.lab.service;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.model.News;
import com.epam.lab.repository.AuthorRepository;
import com.epam.lab.repository.NewsRepository;
import com.epam.lab.repository.TagRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.lab.service.TestUtil.convertToEntity;
import static com.epam.lab.service.TestUtil.getTestNewsDto;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class NewsServiceImplTest {
    private NewsRepository newsRepositoryMock;
    private AuthorRepository authorRepositoryMock;
    private TagRepository tagRepositoryMock;
    private NewsService newsService;

    @Before
    public void setUp() {
        newsRepositoryMock = mock(NewsRepository.class);
        authorRepositoryMock = mock(AuthorRepository.class);
        tagRepositoryMock = mock(TagRepository.class);
        newsService = new NewsServiceImpl(newsRepositoryMock, authorRepositoryMock, tagRepositoryMock);
    }

    @Test
    public void shouldCreateNewsDto() {
        NewsDto newsDto = getTestNewsDto();
        News newsEntity = convertToEntity(newsDto);
        newsEntity.setId(1);
        newsDto.getAuthor().setId(1);
        newsDto.getTags().forEach(tagDto -> tagDto.setId(nextLong(1, Long.MAX_VALUE)));

        when(newsRepositoryMock.save(any())).thenReturn(newsEntity);
        when(authorRepositoryMock.save(any())).thenReturn(convertToEntity(newsDto.getAuthor()));
        newsDto.getTags().forEach(tagDto ->
                when(tagRepositoryMock.save(any())).thenReturn(convertToEntity(tagDto)));

        NewsDto dto = newsService.create(newsDto);

        verify(newsRepositoryMock, times(1)).save(any());
        verify(authorRepositoryMock, times(1)).save(any());
        verify(tagRepositoryMock, times(newsDto.getTags().size())).save(any());
        verifyNoMoreInteractions(newsRepositoryMock);

        assertTrue(dto.getId() > 0);
        assertThat(dto.getTitle(), is(newsDto.getTitle()));
        assertThat(dto.getFullText(), is(newsDto.getFullText()));
    }

    @Test
    public void shouldReadNewsDto() {
        NewsDto newsDto = getTestNewsDto();
        newsDto.setId(nextLong(1, Long.MAX_VALUE));
        News newsEntity = convertToEntity(newsDto);

        when(newsRepositoryMock.findById(anyLong())).thenReturn(Optional.of(newsEntity));

        Optional<NewsDto> dtoOptional = newsService.read(newsDto);

        verify(newsRepositoryMock, times(1)).findById(anyLong());
        verifyNoMoreInteractions(newsRepositoryMock);

        assertTrue(dtoOptional.isPresent());
    }

    @Test
    public void shouldUpdateNewsDto() {
        NewsDto newsDto = getTestNewsDto();
        newsDto.setId(nextLong(1, Long.MAX_VALUE));
        News newsEntity = convertToEntity(newsDto);
        newsEntity.setTitle(random(10, true, false));

        when(newsRepositoryMock.update(any())).thenReturn(newsEntity);
        when(newsRepositoryMock.findById(newsDto.getId())).thenReturn(Optional.of(newsEntity));
        when(authorRepositoryMock.save(any())).thenReturn(convertToEntity(newsDto.getAuthor()));
        newsDto.getTags().forEach(tagDto ->
                when(tagRepositoryMock.save(any())).thenReturn(convertToEntity(tagDto)));

        Optional<NewsDto> optionalNewsDto = newsService.update(newsDto);

        verify(newsRepositoryMock, times(1)).findById(newsDto.getId());
        verify(newsRepositoryMock, times(1)).update(any());
        verify(authorRepositoryMock, times(1)).save(any());
        verify(tagRepositoryMock, times(newsDto.getTags().size())).save(any());
        verifyNoMoreInteractions(newsRepositoryMock);

        assertTrue(optionalNewsDto.isPresent());
        assertTrue(optionalNewsDto.get().getId() > 0);
        assertThat(optionalNewsDto.get().getTitle(), not(newsDto.getTitle()));
        assertThat(optionalNewsDto.get().getFullText(), Matchers.is(newsDto.getFullText()));
    }

    @Test
    public void shouldDeleteNewsDto() {
        NewsDto newsDto = getTestNewsDto();
        newsDto.setId(nextLong(1, Long.MAX_VALUE));
        News newsEntity = convertToEntity(newsDto);

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

        when(newsRepositoryMock.findAll(any(), any(), any(), any())).thenReturn(Collections.singletonList(newsEntity));

        List<String> authorsName = singletonList(newsDto.getAuthor().getName());
        List<String> tagsName = singletonList(random(5));
        List<String> sort = singletonList("author_name");

        List<NewsDto> news = newsService.findNews(authorsName, tagsName, sort, 10, 1);

        verify(newsRepositoryMock, times(1)).findAll(any(), any(), anyInt(), anyInt());
        verifyNoMoreInteractions(newsRepositoryMock);

        assertTrue(news.size() > 0);
    }
}
