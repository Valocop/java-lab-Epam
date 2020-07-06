package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.model.Author;
import com.epam.lab.repository.AuthorRepo;
import com.epam.lab.repository.NewsRepo;
import com.epam.lab.specification.FindSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthorServiceImplTest {
    private static AuthorDto testAuthorDto = new AuthorDto(1, "Test", "Test");
    private static Author testAuthorOne = new Author(1, "Test1", "Test1");
    private static Author testAuthorTwo = new Author(2, "Test2", "Test2");
    private AuthorService authorService;
    private AuthorRepo authorRepo;

    private static List<Author> testAuthorList() {
        return new ArrayList<Author>() {{
            add(testAuthorOne);
            add(testAuthorTwo);
        }};
    }

    @Before
    public void setUp() {
        authorRepo = mock(AuthorRepo.class);
        NewsRepo newsRepo = mock(NewsRepo.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        when(modelMapper.map(testAuthorDto, Author.class)).thenReturn(testAuthorOne);
        when(modelMapper.map(testAuthorOne, AuthorDto.class)).thenReturn(testAuthorDto);
        authorService = new AuthorServiceImpl(authorRepo, newsRepo);
        authorService.setModelMapper(modelMapper);
        when(authorRepo.save(testAuthorOne)).thenReturn((long) 1);
        when(authorRepo.update(testAuthorOne)).thenReturn(true);
        when(authorRepo.delete(testAuthorOne)).thenReturn(true);
        when(authorRepo.findBy(any())).thenReturn(testAuthorList());
    }

    @Test
    public void shouldCreateAuthor() {
        long id = authorService.create(testAuthorDto);
        verify(authorRepo, times(1)).save(testAuthorOne);
        Assert.assertTrue(id > 0);
    }

    @Test
    public void shouldUpdateAuthor() {
        Optional<AuthorDto> dtoOptional = authorService.update(testAuthorDto);
        verify(authorRepo, times(1)).update(testAuthorOne);
        Assert.assertTrue(dtoOptional.isPresent());
        Assert.assertEquals(testAuthorDto, dtoOptional.get());
    }

    @Test
    public void shouldRemoveAuthor() {
        boolean isRemoved = authorService.remove(testAuthorDto);
        verify(authorRepo, times(1)).delete(testAuthorOne);
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void shouldFindAuthorById() {
        Optional<AuthorDto> authorDto = authorService.findById(1);
        verify(authorRepo, times(1)).findBy(any(FindSpecification.class));
        Assert.assertNotNull(authorDto);
        Assert.assertEquals(testAuthorDto, authorDto.get());
    }

    @Test
    public void shouldFindAuthorByNameAndId() {
        Optional<AuthorDto> dtoOptional = authorService.findByIdAndName(testAuthorDto.getId(), testAuthorDto.getName());
        verify(authorRepo, times(1)).findBy(any(FindSpecification.class));
        Assert.assertTrue(dtoOptional.isPresent());
        Assert.assertEquals(testAuthorDto, dtoOptional.get());
    }

    @Test
    public void shouldFindByNewsId() {
        List<AuthorDto> authorDtoList = authorService.findByNewsId(anyLong());
        verify(authorRepo, times(1)).findBy(any());
        Assert.assertNotNull(authorDtoList);
        Assert.assertEquals(2, authorDtoList.size());
        Assert.assertEquals(testAuthorDto, authorDtoList.get(0));
    }
}
