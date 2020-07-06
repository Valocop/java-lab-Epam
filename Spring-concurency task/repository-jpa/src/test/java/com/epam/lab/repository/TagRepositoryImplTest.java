package com.epam.lab.repository;

import com.epam.lab.configuration.SpringRepoConfig;
import com.epam.lab.model.Tag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.lab.repository.TestUtil.getTestTag;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringRepoConfig.class},
        loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("dev")
@Transactional
public class TagRepositoryImplTest {
    @Resource
    private TagRepository tagRepository;

    @Test
    public void shouldSaveTag() {
        Tag tag = getTestTag();
        Tag savedTag = tagRepository.save(tag);
        Assert.assertNotNull(savedTag);
        Optional<Tag> optionalTag = tagRepository.findById(savedTag.getId());
        Assert.assertTrue(optionalTag.isPresent());
        Assert.assertEquals(savedTag, optionalTag.get());
    }

    @Test
    public void shouldUpdateTag() {
        Tag tag = getTestTag();
        Tag savedTag = tagRepository.save(tag);
        Assert.assertNotNull(savedTag);
        tag.setName("Another");
        Tag updatedTag = tagRepository.update(savedTag);
        Assert.assertNotNull(updatedTag);
        Optional<Tag> optionalTag = tagRepository.findById(updatedTag.getId());
        Assert.assertTrue(optionalTag.isPresent());
        Assert.assertEquals(updatedTag, optionalTag.get());
    }

    @Test
    public void shouldDeleteTag() {
        Tag tag = getTestTag();
        Tag savedTag = tagRepository.save(tag);
        Assert.assertNotNull(savedTag);
        tagRepository.delete(savedTag);
        Optional<Tag> optionalTag = tagRepository.findById(savedTag.getId());
        Assert.assertFalse(optionalTag.isPresent());
    }

    @Test
    public void shouldReadAllTags() {
        Tag tagOne = getTestTag();
        Tag tagTwo = getTestTag();
        Tag tagThree = getTestTag();
        tagRepository.save(tagOne);
        tagRepository.save(tagTwo);
        tagRepository.save(tagThree);
        List<Tag> testTagList = Arrays.asList(tagOne, tagTwo, tagThree);
        List<Tag> tagList = tagRepository.readAll();
        Assert.assertEquals(testTagList, tagList);
    }
}
