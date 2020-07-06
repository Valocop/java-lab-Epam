package com.epam.lab.service;

import com.epam.lab.dto.UserDto;

import java.util.Optional;

public interface UserService extends Service<UserDto> {

    Optional<UserDto> getByUserName(String userName);
}
