package com.epam.lab.repository;

import com.epam.lab.configuration.SpringRepoConfig;
import com.epam.lab.model.Role;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

import static com.epam.lab.repository.TestUtil.getTestRole;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringRepoConfig.class},
        loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("dev")
@Transactional
public class RoleRepositoryImplTest {
    @Resource
    private RoleRepository roleRepository;

    @Test
    public void shouldSaveRole() {
        Role role = getTestRole();
        Role savedRole = roleRepository.save(role);
        Assert.assertNotNull(savedRole);
        Optional<Role> roleOptional = roleRepository.findById(role.getId());
        Assert.assertTrue(roleOptional.isPresent());
        Assert.assertEquals(savedRole, roleOptional.get());
    }

    @Test
    public void shouldUpdateUser() {
        Role role = getTestRole();
        Role savedRole = roleRepository.save(role);
        Assert.assertNotNull(savedRole);
        savedRole.setName("New Name");
        Role updatedRole = roleRepository.update(savedRole);
        Assert.assertNotNull(updatedRole);
        Optional<Role> optionalRole = roleRepository.findById(role.getId());
        Assert.assertTrue(optionalRole.isPresent());
        Assert.assertEquals(updatedRole, optionalRole.get());
    }

    @Test
    public void shouldDeleteUser() {
        Role role = getTestRole();
        Role savedRole = roleRepository.save(role);
        Assert.assertNotNull(savedRole);
        Optional<Role> roleOptional = roleRepository.findById(role.getId());
        Assert.assertTrue(roleOptional.isPresent());
        roleRepository.delete(role);
        Optional<Role> deletedRoleOptional = roleRepository.findById(role.getId());
        Assert.assertFalse(deletedRoleOptional.isPresent());
    }
}
