package com.example.netflix.services;

import com.example.netflix.models.User;
import com.example.netflix.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }



    public User addUser(User user) {

        return userRepository.save(user);
    }
    public User modifyUser(User user) {
        return userRepository.save(user);
    }
    public void deleteUser(long id) {
        var existingUser = userRepository.findById(id);
        if (existingUser==null) {
            return;

        }
        userRepository.deleteById(id);
    }
}
