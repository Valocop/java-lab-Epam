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
import org.mockito.Mockito;

import java.util.Optional;

import static com.epam.lab.service.TestUtil.convertToEntity;
import static com.epam.lab.service.TestUtil.getTestAuthorDto;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class AuthorServiceImplTest {
    private AuthorRepository mockAuthorRepository;
    private AuthorService authorService;

    @Before
    public void setUp() {
        mockAuthorRepository = Mockito.mock(AuthorRepository.class);
        authorService = new AuthorServiceImpl(mockAuthorRepository);
    }

    @Test
    public void shouldCreateAuthorDto() {
        AuthorDto authorDto = getTestAuthorDto();
        Author authorEntity = convertToEntity(authorDto);

        Mockito.when(mockAuthorRepository.save(authorEntity)).thenReturn(authorEntity);

        authorService.create(authorDto);

        ArgumentCaptor<Author> argumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(mockAuthorRepository, times(1)).save(argumentCaptor.capture());
        verifyNoMoreInteractions(mockAuthorRepository);

        Author author = argumentCaptor.getValue();

        assertThat(author.getName(), is(authorDto.getName()));
        assertThat(author.getSurname(), is(authorDto.getSurname()));
    }


    @Test
    public void shouldReadAuthorDto() {
        AuthorDto authorDto = getTestAuthorDto();
        authorDto.setId(nextLong(1, Long.MAX_VALUE));
        Author authorEntity = convertToEntity(authorDto);

        when(mockAuthorRepository.findById(anyLong())).thenReturn(Optional.of(authorEntity));

        Optional<AuthorDto> dtoOptional = authorService.read(authorDto);

        verify(mockAuthorRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(mockAuthorRepository);

        assertTrue(dtoOptional.isPresent());
    }

    @Test
    public void shouldUpdateAuthorName() {
        AuthorDto authorDto = getTestAuthorDto();
        authorDto.setId(nextLong(1, Long.MAX_VALUE));
        Author authorEntity = convertToEntity(authorDto);
        authorEntity.setName(RandomStringUtils.random(10, true, false));

        when(mockAuthorRepository.update(any(Author.class))).thenReturn(authorEntity);
        when(mockAuthorRepository.findById(authorDto.getId())).thenReturn(Optional.of(authorEntity));

        Optional<AuthorDto> dtoOptional = authorService.update(authorDto);

        verify(mockAuthorRepository, times(1)).findById(authorDto.getId());
        verify(mockAuthorRepository, times(1)).update(any(Author.class));
        verifyNoMoreInteractions(mockAuthorRepository);

        assertTrue(dtoOptional.isPresent());
        assertTrue(dtoOptional.get().getId() > 0);
        assertThat(dtoOptional.get().getName(), not(authorDto.getName()));
        assertThat(dtoOptional.get().getSurname(), is(authorDto.getSurname()));
    }

    @Test
    public void shouldDeleteAuthor() {
        AuthorDto authorDto = getTestAuthorDto();
        authorDto.setId(nextLong(1, Long.MAX_VALUE));
        Author authorEntity = convertToEntity(authorDto);

        authorService.delete(authorDto);

        verify(mockAuthorRepository, times(1)).delete(authorEntity);
        verifyNoMoreInteractions(mockAuthorRepository);
    }
}
