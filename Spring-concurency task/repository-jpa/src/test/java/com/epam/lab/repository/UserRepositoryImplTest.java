package com.epam.lab.repository;

import com.epam.lab.configuration.SpringRepoConfig;
import com.epam.lab.model.Role;
import com.epam.lab.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Optional;

import static com.epam.lab.repository.TestUtil.getTestRole;
import static com.epam.lab.repository.TestUtil.getTestUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringRepoConfig.class},
        loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("dev")
@Transactional
public class UserRepositoryImplTest {
    @Resource
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser() {
        User user = getTestUser();
        User savedUser = userRepository.save(user);
        Assert.assertNotNull(savedUser);
        Optional<User> optionalUser = userRepository.findById(savedUser.getId());
        Assert.assertTrue(optionalUser.isPresent());
        Assert.assertEquals(user, optionalUser.get());
    }

    @Test
    public void shouldUpdateUser() {
        User user = getTestUser();
        User savedUser = userRepository.save(user);
        Assert.assertNotNull(savedUser);
        savedUser.setUsername("New Name");
        savedUser.setSurname("New Surname");
        savedUser.setPassword("New password");
        userRepository.update(savedUser);
        Optional<User> optionalUser = userRepository.findById(savedUser.getId());
        Assert.assertTrue(optionalUser.isPresent());
        Assert.assertEquals(savedUser, optionalUser.get());
    }

    @Test
    public void shouldDeleteUser() {
        User user = getTestUser();
        User savedUser = userRepository.save(user);
        Assert.assertNotNull(savedUser);
        userRepository.delete(savedUser);
        Optional<User> optionalUser = userRepository.findById(savedUser.getId());
        Assert.assertFalse(optionalUser.isPresent());
    }

    @Test
    public void shouldFindUserByUsername() {
        User user = getTestUser();
        String username = "User Test";
        user.setUsername(username);
        User savedUser = userRepository.save(user);
        Assert.assertNotNull(savedUser);
        Optional<User> optionalUser = userRepository.findByUsername(username);
        Assert.assertTrue(optionalUser.isPresent());
        Assert.assertEquals(username, optionalUser.get().getUsername());
    }

    @Test
    public void shouldGetUserWithRole() {
        User user = getTestUser();
        Role role = getTestRole();
        user.setRoles(Collections.singletonList(role));
        User savedUser = userRepository.save(user);
        Assert.assertNotNull(savedUser);
        Assert.assertEquals(role, savedUser.getRoles().get(0));
    }
}
