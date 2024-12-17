package com.example.netflix.services.Impls;

import com.example.netflix.dtos.UserDto;
import com.example.netflix.dtos.UserListingDto;
import com.example.netflix.exceptions.EmailExistException;
import com.example.netflix.mappers.UserMapper;
import com.example.netflix.mappers.UserMapperImpl;
import com.example.netflix.models.User;
import com.example.netflix.repositories.UserRepository;
import com.example.netflix.services.UserServiceLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserServiceLogin {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserMapperImpl userMapperImpl;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, UserMapperImpl userMapperImpl) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;

        // Optionally, you can add a default user setup logic here like in the first example.
        this.userMapperImpl = userMapperImpl;
    }

    @Override
    public UserDto login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null){
            System.out.println("User not found!");
            return null;
        }
        if (!user.getPassword().equals(password)){
            System.out.println("Password incorrect!");
            return null;
        }
        return userMapperImpl.toDto(user);
    }

}


