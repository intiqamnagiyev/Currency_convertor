package com.example.currency_exchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonForm {
    private String fullName;
    private String  email;
    private String  password;
    private String  passwordConfirm;

}
