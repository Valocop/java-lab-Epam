package com.epam.lab.service;

import com.epam.lab.dto.TagDto;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.NewsRepo;
import com.epam.lab.repository.TagRepo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceImplTest {
    private TagRepo tagRepo;
    private NewsRepo newsRepo;
    private TagService tagService;
    private Tag tagTestOne = new Tag(1, "Tag");
    private Tag tagTestTwo = new Tag(2, "Tag2");
    private TagDto tagDtoTest = new TagDto(1, "TagDto");

    @Before
    public void setUp() {
        tagRepo = mock(TagRepo.class);
        newsRepo = mock(NewsRepo.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        when(modelMapper.map(tagDtoTest, Tag.class)).thenReturn(tagTestOne);
        when(modelMapper.map(tagTestOne, TagDto.class)).thenReturn(tagDtoTest);
        tagService = new TagServiceImpl(tagRepo, newsRepo);
        tagService.setModelMapper(modelMapper);
        when(tagRepo.save(tagTestOne)).thenReturn(10L);
        when(tagRepo.update(tagTestOne)).thenReturn(true);
        when(tagRepo.delete(tagTestOne)).thenReturn(true);
        when(tagRepo.findBy(any())).thenReturn(getTestTagList());
        when(tagRepo.deleteUnsignedTags()).thenReturn(true);
    }

    @Test
    public void shouldCreateTag() {
        long id = tagService.create(tagDtoTest);
        verify(tagRepo, times(1)).save(tagTestOne);
        Assert.assertTrue(id > 0);
    }

    @Test
    public void shouldUpdateTag() {
        Optional<TagDto> optionalTagDto = tagService.update(tagDtoTest);
        verify(tagRepo, times(1)).update(tagTestOne);
        Assert.assertTrue(optionalTagDto.isPresent());
        Assert.assertEquals(tagDtoTest, optionalTagDto.get());
    }

    @Test
    public void shouldRemoveTag() {
        boolean isRemoved = tagService.remove(tagDtoTest);
        verify(tagRepo, times(1)).delete(tagTestOne);
        Assert.assertTrue(isRemoved);
    }

    @Test
    public void shouldFindTagById() {
        TagDto tagDto = tagService.findById(anyLong());
        verify(tagRepo, times(1)).findBy(any());
        Assert.assertNotNull(tagDto);
        Assert.assertEquals(tagDtoTest, tagDto);
    }

    @Test
    public void shouldFindByName() {
        List<TagDto> tagDtoList = tagService.findByName(anyString());
        verify(tagRepo, times(1)).findBy(any());
        Assert.assertNotNull(tagDtoList);
        Assert.assertEquals(tagDtoTest, tagDtoList.get(0));
    }

    @Test
    public void shouldFindTagsByNewsId() {
        List<TagDto> tagDtoList = tagService.findByNewsId(anyLong());
        verify(tagRepo, times(1)).findBy(any());
        Assert.assertNotNull(tagDtoList);
        Assert.assertEquals(tagDtoTest, tagDtoList.get(0));
    }

    @Test
    public void shouldDeleteUnsignedTags() {
        boolean isDeleted = tagService.deleteUnsignedTags();
        verify(tagRepo, times(1)).deleteUnsignedTags();
        Assert.assertTrue(isDeleted);
    }

    private List<Tag> getTestTagList() {
        return new ArrayList<Tag>() {{
            add(tagTestOne);
            add(tagTestTwo);
        }};
    }
}
