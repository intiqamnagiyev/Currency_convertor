package com.example.currency_exchange.service;

import com.example.currency_exchange.entity.User;
import com.example.currency_exchange.model.LoginUser;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User save(User user);

    User getUserByEmail(LoginUser loginUser);

    boolean checkEmail(String email);
}
