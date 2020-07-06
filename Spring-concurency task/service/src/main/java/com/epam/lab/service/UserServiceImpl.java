package com.epam.lab.service;

import com.epam.lab.dto.UserDto;
import com.epam.lab.model.User;
import com.epam.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static com.epam.lab.service.ServiceUtil.convertToDto;
import static com.epam.lab.service.ServiceUtil.convertToEntity;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(s);
        return userOptional.map(ServiceUtil::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("User with username - " + s + "not found!"));
    }

    @Override
    public UserDto create(UserDto dto) {
        User user = userRepository.save(convertToEntity(dto));
        return convertToDto(user);
    }

    @Override
    public Optional<UserDto> read(UserDto dto) {
        Optional<User> optionalUser = userRepository.findById(dto.getId());
        return optionalUser.map(ServiceUtil::convertToDto);
    }

    @Override
    public Optional<UserDto> update(UserDto dto) {
        User updatedUser = userRepository.update(convertToEntity(dto));
        return Optional.of(convertToDto(updatedUser));
    }

    @Override
    public void delete(UserDto dto) {
        userRepository.delete(convertToEntity(dto));
    }

    @Override
    public Optional<UserDto> getByUserName(String userName) {
        try {
            return Optional.of(this.loadUserByUsername(userName));
        } catch (EntityNotFoundException e) {
            return Optional.empty();
        }
    }
}
