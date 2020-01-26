package com.example.bullcow.controller;

import com.example.bullcow.domain.User;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, Resource<User>> {
    @Override
    public Resource<User> toResource(User user) {

        return new Resource<>(user,
                linkTo(
                        methodOn(MainController.class)
                                .oneUser(user.getId()))
                        .withSelfRel(),
                linkTo(
                        methodOn(MainController.class)
                                .allUsers())
                        .withRel("/"));
    }
}
