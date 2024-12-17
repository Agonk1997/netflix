package com.example.netflix.mappers;

import com.example.netflix.dtos.UserDto;
import com.example.netflix.dtos.UserListingDto;
import com.example.netflix.infrastructure.Convert;
import com.example.netflix.models.User;

public interface UserMapper extends Convert<UserDto, User> {
    UserListingDto toUserListingDto(User user);
}
