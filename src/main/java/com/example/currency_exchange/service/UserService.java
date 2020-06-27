package com.example.currency_exchange.service;

import com.example.currency_exchange.entity.User;
import com.example.currency_exchange.ex.UserNotFoundEx;
import com.example.currency_exchange.model.LoginUser;
import com.example.currency_exchange.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
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

    public User getUserByEmail(LoginUser loginUser) {
        return userRepository.findByEmail(loginUser.getEmail())
                .filter(u-> BCrypt.checkpw(loginUser.getPassword(),u.getPassword()))
                .orElseThrow(UserNotFoundEx::new);

    }
    public boolean checkEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

}
