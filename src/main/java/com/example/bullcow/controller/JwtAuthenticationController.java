package com.example.bullcow.controller;


import com.example.bullcow.domain.Role;
import com.example.bullcow.domain.User;
import com.example.bullcow.exceptions.UserAlreadyExistException;
import com.example.bullcow.modules.NumberProcessor;
import com.example.bullcow.repos.UserRepo;
import com.example.bullcow.security.LoginData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/auth")
public class JwtAuthenticationController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserResourceAssembler userResourceAssembler;


    @PostMapping("/registration")
    public ResponseEntity saveUser(@RequestBody @Valid LoginData loginData) throws Exception {

        User userFromDb = userRepo.findByUsername(loginData.getUsername());

        if (userFromDb != null) {
            throw new UserAlreadyExistException("User already exists!");
        }

        String hashedPassword = passwordEncoder.encode(loginData.getPassword());

        User user = new User();

        user.setUsername(loginData.getUsername());
        user.setPassword(hashedPassword);
        user.setRoles(Collections.singleton(Role.USER));
        user.setGeneratedNumber(NumberProcessor.generateNumber());
        log.info("On-registration generated number:" + user.getGeneratedNumber());
        userRepo.save(user);



        Resource<User> resource = userResourceAssembler.toResource(user);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

}
