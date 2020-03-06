package com.epam.lab.service;

import com.epam.lab.dto.TagDto;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.TagRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static com.epam.lab.service.TestUtil.convertToEntity;
import static com.epam.lab.service.TestUtil.getTestTagDto;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class TagServiceImplTest {
    private TagService tagService;
    private ModelMapper modelMapperMock;
    private TagRepository tagRepositoryMock;

    @Before
    public void setUp() {
        tagRepositoryMock = mock(TagRepository.class);
        modelMapperMock = mock(ModelMapper.class);
        tagService = new TagServiceImpl(tagRepositoryMock, modelMapperMock);
    }

    @Test
    public void shouldCreateTagDto() {
        TagDto tagDto = getTestTagDto();
        Tag tagEntity = TestUtil.convertToEntity(tagDto);
        tagEntity.setId(nextLong(1, Long.MAX_VALUE));

        when(modelMapperMock.map(tagDto, Tag.class)).thenReturn(tagEntity);
        when(tagRepositoryMock.save(any())).thenReturn(tagEntity);

        tagService.create(tagDto);

        ArgumentCaptor<Tag> argumentCaptor = ArgumentCaptor.forClass(Tag.class);
        verify(tagRepositoryMock, times(1)).save(argumentCaptor.capture());
        verifyNoMoreInteractions(tagRepositoryMock);

        Tag tag = argumentCaptor.getValue();

        assertTrue(tag.getId() > 0);
        assertThat(tag.getName(), is(tagDto.getName()));
    }

    @Test
    public void shouldReadTagDto() {
        TagDto tagDto = getTestTagDto();
        tagDto.setId(nextLong(1, Long.MAX_VALUE));
        Tag tagEntity = convertToEntity(tagDto);

        when(modelMapperMock.map(tagDto, Tag.class)).thenReturn(tagEntity);
        when(modelMapperMock.map(tagEntity, TagDto.class)).thenReturn(tagDto);
        when(tagRepositoryMock.findById(anyLong())).thenReturn(Optional.of(tagEntity));

        Optional<TagDto> dtoOptional = tagService.read(tagDto);

        verify(tagRepositoryMock, times(1)).findById(anyLong());
        verifyNoMoreInteractions(tagRepositoryMock);

        assertTrue(dtoOptional.isPresent());
        assertEquals(tagDto, dtoOptional.get());
    }

    @Test
    public void shouldUpdateTagName() {
        TagDto tagDto = getTestTagDto();
        tagDto.setId(nextLong(1, Long.MAX_VALUE));
        Tag tagEntity = convertToEntity(tagDto);
        tagEntity.setName(RandomStringUtils.random(10, true, false));

        when(modelMapperMock.map(tagDto, Tag.class)).thenReturn(tagEntity);
        when(modelMapperMock.map(tagEntity, TagDto.class)).thenReturn(tagDto);
        when(tagRepositoryMock.update(tagEntity)).thenReturn(tagEntity);
        when(tagRepositoryMock.findById(tagDto.getId())).thenReturn(Optional.of(tagEntity));

        tagService.update(tagDto);

        ArgumentCaptor<Tag> argumentCaptor = ArgumentCaptor.forClass(Tag.class);
        verify(tagRepositoryMock, times(1)).findById(tagDto.getId());
        verify(tagRepositoryMock, times(1)).update(argumentCaptor.capture());
        verifyNoMoreInteractions(tagRepositoryMock);

        Tag tag = argumentCaptor.getValue();

        assertTrue(tag.getId() > 0);
        assertThat(tag.getName(), not(tagDto.getName()));
    }

    @Test
    public void shouldDeleteTag() {
        TagDto tagDto = getTestTagDto();
        tagDto.setId(nextLong(1, Long.MAX_VALUE));
        Tag tagEntity = convertToEntity(tagDto);

        when(modelMapperMock.map(tagDto, Tag.class)).thenReturn(tagEntity);
        when(modelMapperMock.map(tagEntity, TagDto.class)).thenReturn(tagDto);

        tagService.delete(tagDto);

        verify(tagRepositoryMock, times(1)).delete(tagEntity);
        verifyNoMoreInteractions(tagRepositoryMock);
    }
}
