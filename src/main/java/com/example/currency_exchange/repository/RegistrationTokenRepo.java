package com.example.currency_exchange.repository;

import com.example.currency_exchange.entity.RegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationTokenRepo extends JpaRepository<RegistrationToken,Integer> {
    RegistrationToken findByToken(String token);
}
