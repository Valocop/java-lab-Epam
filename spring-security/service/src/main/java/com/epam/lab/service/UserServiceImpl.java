package com.epam.lab.service;

import com.epam.lab.model.User;
import com.epam.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserServiceImpl implements UserService, UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User dto) {
        return null;
    }

    @Override
    public Optional<User> read(User dto) {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User dto) {
        return Optional.empty();
    }

    @Override
    public void delete(User dto) {

    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
