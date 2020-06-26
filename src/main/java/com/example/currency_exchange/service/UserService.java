package com.example.currency_exchange.service;

import com.example.currency_exchange.entity.User;
import com.example.currency_exchange.model.LoginUser;
import com.example.currency_exchange.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(LoginUser loginUser) {
        return userRepository.findByEmail(loginUser.getEmail());
    }
}
