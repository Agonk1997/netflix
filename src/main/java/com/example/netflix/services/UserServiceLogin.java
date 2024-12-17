package com.example.netflix.services;

import com.example.netflix.dtos.UserDto;

public interface UserServiceLogin {
    UserDto login(String email, String password);
}
