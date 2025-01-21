package com.example.netflix.services.Impls;

import com.example.netflix.dtos.UserDto;
import com.example.netflix.mappers.UserMapperImpl;
import com.example.netflix.models.User;
import com.example.netflix.repositories.UserRepository;
import com.example.netflix.services.UserServiceLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Qualifier("userService")
public class UserServiceImpl implements UserServiceLogin {

    private final UserRepository userRepository;
    private final UserMapperImpl userMapperImpl;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapperImpl userMapperImpl, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapperImpl = userMapperImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            System.out.println("User not found!");
            return null;
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("Password incorrect!");
            return null;
        }
        return userMapperImpl.toDto(user);
    }
}