package com.epam.lab.service;

import com.epam.lab.dto.TagDto;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.TagRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Ignore;
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
    private TagRepository tagRepositoryMock;

    @Before
    public void setUp() {
        tagRepositoryMock = mock(TagRepository.class);
        tagService = new TagServiceImpl(tagRepositoryMock);
    }

    @Test
    public void shouldCreateTagDto() {
        TagDto tagDto = getTestTagDto();
        Tag tagEntity = TestUtil.convertToEntity(tagDto);
        tagEntity.setId(nextLong(1, Long.MAX_VALUE));

        when(tagRepositoryMock.save(any())).thenReturn(tagEntity);

        TagDto dto = tagService.create(tagDto);


        verify(tagRepositoryMock, times(1)).save(any());
        verifyNoMoreInteractions(tagRepositoryMock);

        assertTrue(dto.getId() > 0);
        assertThat(dto.getName(), is(tagDto.getName()));
    }

    @Test
    public void shouldReadTagDto() {
        TagDto tagDto = getTestTagDto();
        tagDto.setId(nextLong(1, Long.MAX_VALUE));
        Tag tagEntity = convertToEntity(tagDto);

        when(tagRepositoryMock.findById(anyLong())).thenReturn(Optional.of(tagEntity));

        Optional<TagDto> dtoOptional = tagService.read(tagDto);

        verify(tagRepositoryMock, times(1)).findById(anyLong());
        verifyNoMoreInteractions(tagRepositoryMock);

        assertTrue(dtoOptional.isPresent());
        assertEquals(tagDto.getName(), dtoOptional.get().getName());
    }

    @Test
    public void shouldUpdateTagName() {
        TagDto tagDto = getTestTagDto();
        tagDto.setId(nextLong(1, Long.MAX_VALUE));
        Tag tagEntity = convertToEntity(tagDto);
        tagEntity.setName(RandomStringUtils.random(10, true, false));

        when(tagRepositoryMock.update(any())).thenReturn(tagEntity);
        when(tagRepositoryMock.findById(tagDto.getId())).thenReturn(Optional.of(tagEntity));

        Optional<TagDto> tagDtoOptional = tagService.update(tagDto);

        verify(tagRepositoryMock, times(1)).findById(tagDto.getId());
        verify(tagRepositoryMock, times(1)).update(any());
        verifyNoMoreInteractions(tagRepositoryMock);

        assertTrue(tagDtoOptional.isPresent());
        assertTrue(tagDtoOptional.get().getId() > 0);
        assertThat(tagDtoOptional.get().getName(), not(tagDto.getName()));
    }

    @Test
    public void shouldDeleteTag() {
        TagDto tagDto = getTestTagDto();
        tagDto.setId(nextLong(1, Long.MAX_VALUE));
        Tag tagEntity = convertToEntity(tagDto);

        tagService.delete(tagDto);

        verify(tagRepositoryMock, times(1)).delete(tagEntity);
        verifyNoMoreInteractions(tagRepositoryMock);
    }
}
