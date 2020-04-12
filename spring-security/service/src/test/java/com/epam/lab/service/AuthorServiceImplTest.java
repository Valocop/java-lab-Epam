package com.epam.lab.service;

import com.epam.lab.configuration.SpringRepoConfig;
import com.epam.lab.dto.AuthorDto;
import com.epam.lab.model.Author;
import com.epam.lab.repository.AuthorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static com.epam.lab.service.TestUtil.convertToEntity;
import static com.epam.lab.service.TestUtil.getTestAuthorDto;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringRepoConfig.class},
        loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("dev")
public class AuthorServiceImplTest {
    @Autowired
    private AuthorRepository authorRepository;
    private AuthorService authorService;
    private AuthorRepository spyAuthorRepository;

    @Before
    public void setUp() {
//        authorRepositoryMock = mock(AuthorRepository.class);
//        spyAuthorRepository = Mockito.spy(authorRepository);
        spyAuthorRepository = Mockito.mock(AuthorRepository.class, AdditionalAnswers.delegatesTo(authorRepository));
        authorService = new AuthorServiceImpl(spyAuthorRepository);
    }

    @Test
    public void shouldCreateAuthorDto() {
        AuthorDto authorDto = getTestAuthorDto();
        Author authorEntity = convertToEntity(authorDto);

        Mockito.when(spyAuthorRepository.save(authorEntity)).thenReturn(authorEntity);

        authorService.create(authorDto);

        ArgumentCaptor<Author> argumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(spyAuthorRepository, times(1)).save(argumentCaptor.capture());
        verifyNoMoreInteractions(spyAuthorRepository);

        Author author = argumentCaptor.getValue();

        assertTrue(author.getId() > 0);
        assertThat(author.getName(), is(authorDto.getName()));
        assertThat(author.getSurname(), is(authorDto.getSurname()));
    }

//    @Test
//    public void shouldCreateAuthorDto() {
//        AuthorDto authorDto = getTestAuthorDto();
//        Author authorEntity = convertToEntity(authorDto);
//        authorEntity.setId(1);
//
//        when(modelMapperMock.map(authorDto, Author.class)).thenReturn(authorEntity);
//        when(authorRepositoryMock.save(any())).thenReturn(authorEntity);
//
//        authorService.create(authorDto);
//
//        ArgumentCaptor<Author> argumentCaptor = ArgumentCaptor.forClass(Author.class);
//        verify(authorRepositoryMock, times(1)).save(argumentCaptor.capture());
//        verifyNoMoreInteractions(authorRepositoryMock);
//
//        Author author = argumentCaptor.getValue();
//
//        assertTrue(author.getId() > 0);
//        assertThat(author.getName(), is(authorDto.getName()));
//        assertThat(author.getSurname(), is(authorDto.getSurname()));
//    }
//
//    @Test
//    public void shouldReadAuthorDto() {
//        AuthorDto authorDto = getTestAuthorDto();
//        authorDto.setId(nextLong(1, Long.MAX_VALUE));
//        Author authorEntity = convertToEntity(authorDto);
//
//        when(modelMapperMock.map(authorDto, Author.class)).thenReturn(authorEntity);
//        when(modelMapperMock.map(authorEntity, AuthorDto.class)).thenReturn(authorDto);
//        when(authorRepositoryMock.findById(anyLong())).thenReturn(Optional.of(authorEntity));
//
//        Optional<AuthorDto> dtoOptional = authorService.read(authorDto);
//
//        verify(authorRepositoryMock, times(1)).findById(anyLong());
//        verifyNoMoreInteractions(authorRepositoryMock);
//
//        assertTrue(dtoOptional.isPresent());
//        assertEquals(authorDto, dtoOptional.get());
//    }
//
//    @Test
//    public void shouldUpdateAuthorName() {
//        AuthorDto authorDto = getTestAuthorDto();
//        authorDto.setId(nextLong(1, Long.MAX_VALUE));
//        Author authorEntity = convertToEntity(authorDto);
//        authorEntity.setName(RandomStringUtils.random(10, true, false));
//
//        when(modelMapperMock.map(authorDto, Author.class)).thenReturn(authorEntity);
//        when(modelMapperMock.map(authorEntity, AuthorDto.class)).thenReturn(authorDto);
//        when(authorRepositoryMock.update(authorEntity)).thenReturn(authorEntity);
//        when(authorRepositoryMock.findById(authorDto.getId())).thenReturn(Optional.of(authorEntity));
//
//        authorService.update(authorDto);
//
//        ArgumentCaptor<Author> argumentCaptor = ArgumentCaptor.forClass(Author.class);
//        verify(authorRepositoryMock, times(1)).findById(authorDto.getId());
//        verify(authorRepositoryMock, times(1)).update(argumentCaptor.capture());
//        verifyNoMoreInteractions(authorRepositoryMock);
//
//        Author author = argumentCaptor.getValue();
//
//        assertTrue(author.getId() > 0);
//        assertThat(author.getName(), not(authorDto.getName()));
//        assertThat(author.getSurname(), is(authorDto.getSurname()));
//    }
//
//    @Test
//    public void shouldDeleteAuthor() {
//        AuthorDto authorDto = getTestAuthorDto();
//        authorDto.setId(nextLong(1, Long.MAX_VALUE));
//        Author authorEntity = convertToEntity(authorDto);
//
//        when(modelMapperMock.map(authorDto, Author.class)).thenReturn(authorEntity);
//        when(modelMapperMock.map(authorEntity, AuthorDto.class)).thenReturn(authorDto);
//
//        authorService.delete(authorDto);
//
//        verify(authorRepositoryMock, times(1)).delete(authorEntity);
//        verifyNoMoreInteractions(authorRepositoryMock);
//    }
}
