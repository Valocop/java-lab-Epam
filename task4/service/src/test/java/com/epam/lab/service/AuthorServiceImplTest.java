package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.model.Author;
import com.epam.lab.repository.AuthorRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static com.epam.lab.service.TestUtil.convertToEntity;
import static com.epam.lab.service.TestUtil.getTestAuthorDto;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class AuthorServiceImplTest {
    private AuthorRepository authorRepositoryMock;
    private AuthorService authorService;
    private ModelMapper modelMapperMock;

    @Before
    public void setUp() {
        authorRepositoryMock = mock(AuthorRepository.class);
        modelMapperMock = mock(ModelMapper.class);
        authorService = new AuthorServiceImpl(authorRepositoryMock, modelMapperMock);
    }

    @Test
    public void shouldCreateAuthorDto() {
        AuthorDto authorDto = getTestAuthorDto();
        Author authorEntity = convertToEntity(authorDto);
        authorEntity.setId(1);

        when(modelMapperMock.map(authorDto, Author.class)).thenReturn(authorEntity);
        when(authorRepositoryMock.save(any())).thenReturn(authorEntity);

        authorService.create(authorDto);

        ArgumentCaptor<Author> argumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepositoryMock, times(1)).save(argumentCaptor.capture());
        verifyNoMoreInteractions(authorRepositoryMock);

        Author author = argumentCaptor.getValue();

        assertTrue(author.getId() > 0);
        assertThat(author.getName(), is(authorDto.getName()));
        assertThat(author.getSurname(), is(authorDto.getSurname()));
    }

    @Test
    public void shouldReadAuthorDto() {
        AuthorDto authorDto = getTestAuthorDto();
        authorDto.setId(nextLong(1, Long.MAX_VALUE));
        Author authorEntity = convertToEntity(authorDto);

        when(modelMapperMock.map(authorDto, Author.class)).thenReturn(authorEntity);
        when(modelMapperMock.map(authorEntity, AuthorDto.class)).thenReturn(authorDto);
        when(authorRepositoryMock.findById(anyLong())).thenReturn(Optional.of(authorEntity));

        Optional<AuthorDto> dtoOptional = authorService.read(authorDto);

        verify(authorRepositoryMock, times(1)).findById(anyLong());
        verifyNoMoreInteractions(authorRepositoryMock);

        assertTrue(dtoOptional.isPresent());
        assertEquals(authorDto, dtoOptional.get());
    }

    @Test
    public void shouldUpdateAuthorName() {
        AuthorDto authorDto = getTestAuthorDto();
        authorDto.setId(nextLong(1, Long.MAX_VALUE));
        Author authorEntity = convertToEntity(authorDto);
        authorEntity.setName(RandomStringUtils.random(10, true, false));

        when(modelMapperMock.map(authorDto, Author.class)).thenReturn(authorEntity);
        when(modelMapperMock.map(authorEntity, AuthorDto.class)).thenReturn(authorDto);
        when(authorRepositoryMock.update(authorEntity)).thenReturn(authorEntity);

        authorService.update(authorDto);

        ArgumentCaptor<Author> argumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepositoryMock, times(1)).update(argumentCaptor.capture());
        verifyNoMoreInteractions(authorRepositoryMock);

        Author author = argumentCaptor.getValue();

        assertTrue(author.getId() > 0);
        assertThat(author.getName(), not(authorDto.getName()));
        assertThat(author.getSurname(), is(authorDto.getSurname()));
    }

    @Test
    public void shouldDeleteAuthor() {
        AuthorDto authorDto = getTestAuthorDto();
        authorDto.setId(nextLong(1, Long.MAX_VALUE));
        Author authorEntity = convertToEntity(authorDto);

        when(modelMapperMock.map(authorDto, Author.class)).thenReturn(authorEntity);
        when(modelMapperMock.map(authorEntity, AuthorDto.class)).thenReturn(authorDto);

        authorService.delete(authorDto);

        verify(authorRepositoryMock, times(1)).delete(authorEntity);
        verifyNoMoreInteractions(authorRepositoryMock);
    }
}
