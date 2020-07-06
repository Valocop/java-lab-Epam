package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.model.News;
import com.epam.lab.repository.NewsRepo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NewsServiceImplTest {
    private NewsRepo newsRepo;
    private NewsService newsService;
    private News testNews = getTestNews();
    private NewsDto testNewsDto = getTestNewsDto();

    @Before
    public void setUp() {
        newsRepo = mock(NewsRepo.class);
        AuthorService authorService = mock(AuthorService.class);
        TagService tagService = mock(TagService.class);
        newsService = new NewsServiceImpl(newsRepo, authorService, tagService);
        ModelMapper modelMapper = mock(ModelMapper.class);
        when(modelMapper.map(testNewsDto, News.class)).thenReturn(testNews);
        when(modelMapper.map(testNews, NewsDto.class)).thenReturn(testNewsDto);
        newsService.setModelMapper(modelMapper);
        when(newsRepo.save(testNews)).thenReturn(10L);
        when(authorService.findByIdAndName(anyLong(), anyString())).thenReturn(Optional.empty());
        when(newsRepo.findBy(any())).thenReturn(Collections.singletonList(testNews));
        when(newsRepo.createBindingOfAuthorAndNews(any(), any())).thenReturn(true);
        when(newsRepo.update(testNews)).thenReturn(true);
        when(newsRepo.delete(testNews)).thenReturn(true);
        when(newsRepo.findBy(any())).thenReturn(Collections.singletonList(testNews));
        when(authorService.findByNewsId(1L)).thenReturn(Collections.singletonList(new AuthorDto()));
        when(newsRepo.deleteBindingOfNewsAndTag(any(), any())).thenReturn(true);
    }

    @Test
    public void shouldCreateNews() {
        long id = newsService.create(testNewsDto);
        verify(newsRepo, times(1)).save(testNews);
        Assert.assertTrue(id > 0);
    }

    @Test
    public void shouldUpdateNews() {
        Optional<NewsDto> optionalNewsDto = newsService.update(testNewsDto);
        verify(newsRepo, times(1)).update(any());
        Assert.assertTrue(optionalNewsDto.isPresent());
        Assert.assertEquals(testNewsDto, optionalNewsDto.get());
    }

    @Test
    public void shouldRemoveNews() {
        boolean isRemoved = newsService.remove(testNewsDto);
        verify(newsRepo, times(1)).delete(testNews);
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void shouldFindNewsById() {
        Optional<NewsDto> optionalNewsDto = newsService.findById(1L);
        Assert.assertTrue(optionalNewsDto.isPresent());
    }

    @Test
    public void shouldDeleteTagOfNews() {
        boolean isDeleted = newsService.deleteTagOfNews(1L, 1L);
        Assert.assertTrue(isDeleted);
    }

    private NewsDto getTestNewsDto() {
        AuthorDto authorDto = new AuthorDto(1, "Name", "Surname");
        return new NewsDto(1, "Title", "Short text", "Full text", LocalDate.parse("2019-10-10"),
                LocalDate.parse("2019-10-10"), authorDto, new ArrayList<>());
    }

    private News getTestNews() {
        return new News(1, "Title", "Short text", "Full text",
                LocalDate.parse("2019-10-10"), LocalDate.parse("2019-10-10"));
    }
}
