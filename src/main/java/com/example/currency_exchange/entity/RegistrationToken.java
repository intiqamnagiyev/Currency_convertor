package com.example.currency_exchange.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class RegistrationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @OneToOne(targetEntity = Person.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name="user_id", referencedColumnName = "id")
    private Person person;

    public RegistrationToken(){}

    public RegistrationToken(Person person){
        this.person = person;
        createdTime = new Date();
        token = UUID.randomUUID().toString();
    }

}
