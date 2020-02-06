package com.example.bullcow.controller;

import com.example.bullcow.domain.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserResourceAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User user) {

        return new EntityModel<>(user,
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
