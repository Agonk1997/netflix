package com.example.netflix.mappers;

import com.example.netflix.dtos.UserDto;
import com.example.netflix.dtos.UserListingDto;
import com.example.netflix.models.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class UserMapperImpl implements UserMapper{
    @Override
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        return userDto;
    }


    @Override
    public User toEntity(UserDto Dto) {
        return null;
    }

    @Override
    public UserListingDto toUserListingDto(User user) {
        return null;
    }
}
