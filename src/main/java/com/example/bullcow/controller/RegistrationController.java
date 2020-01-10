package com.example.bullcow.controller;

import com.example.bullcow.domain.Role;
import com.example.bullcow.domain.User;
import com.example.bullcow.modules.NumberProcessor;
import com.example.bullcow.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registration(User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user, BindingResult bindingResult, Map<String, Object> model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "User exists!");
            return "registration";
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());


        user.setGeneratedNumber(NumberProcessor.generateNumber());
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(hashedPassword);
        userRepo.save(user);

        return "redirect:/login";
    }
}
