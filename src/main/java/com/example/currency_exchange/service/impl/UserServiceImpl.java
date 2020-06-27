package com.example.currency_exchange.service.impl;

import com.example.currency_exchange.entity.User;
import com.example.currency_exchange.ex.UserNotFoundEx;
import com.example.currency_exchange.model.LoginUser;
import com.example.currency_exchange.repository.UserRepository;
import com.example.currency_exchange.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(LoginUser loginUser) {
        return userRepository.findByEmail(loginUser.getEmail())
                .filter(u -> BCrypt.checkpw(loginUser.getPassword(), u.getPassword()))
                .orElseThrow(UserNotFoundEx::new);

    }

    @Override
    public boolean checkEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
