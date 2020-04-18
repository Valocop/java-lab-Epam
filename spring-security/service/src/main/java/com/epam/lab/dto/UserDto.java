package com.epam.lab.dto;

import com.epam.lab.validation.CreateValidation;
import com.epam.lab.validation.UpdateValidation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;

public class UserDto implements UserDetails {
    @Min(value = 0,
            groups = UpdateValidation.class,
            message = "id must be positive")
    @Max(value = Long.MAX_VALUE,
            groups = UpdateValidation.class,
            message = "id must be less than " + Long.MAX_VALUE)
    private long id;
    @NotBlank(groups = {CreateValidation.class, UpdateValidation.class},
            message = "name must not be empty")
    @Size(max = 30,
            groups = {CreateValidation.class, UpdateValidation.class},
            message = "name must be less than 30 symbols")
    private String name;
    @NotBlank(groups = {CreateValidation.class, UpdateValidation.class},
            message = "surname must not be empty")
    @Size(max = 30,
            groups = {CreateValidation.class, UpdateValidation.class},
            message = "surname must be less than 30 symbols")
    private String surname;
    @NotBlank(groups = {CreateValidation.class, UpdateValidation.class},
            message = "username must not be empty")
    @Size(max = 30,
            groups = {CreateValidation.class, UpdateValidation.class},
            message = "username must be less than 30 symbols")
    private String username;
    @NotBlank(groups = {CreateValidation.class, UpdateValidation.class},
            message = "password must not be empty")
    @Min(groups = {CreateValidation.class, UpdateValidation.class},
            value = 3, message = "password must be greater than 3 symbols")
    @Size(max = 30,
            groups = {CreateValidation.class, UpdateValidation.class},
            message = "password must be less than 30 symbols")
    private String password;
    private List<@Valid RoleDto> roles;

    public UserDto() {
    }

    public UserDto(long id, String name, String surname, String username, String password, List<RoleDto> roles) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
