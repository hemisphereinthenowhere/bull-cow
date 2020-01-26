package com.example.bullcow.security;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Data
@Component
public
class LoginData {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}