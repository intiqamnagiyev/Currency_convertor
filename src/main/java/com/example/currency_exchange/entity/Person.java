package com.example.currency_exchange.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    private long id;
    private String fullName;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    private String password;

    private boolean isEnabled = false;

}
