package com.example.bullcow.controller;

import com.example.bullcow.domain.Guess;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GuessResourceAssembler implements RepresentationModelAssembler<Guess, EntityModel<Guess>> {


    @Override
    public EntityModel<Guess> toModel(Guess guess) {

        return new EntityModel<>(guess,
                linkTo(
                        methodOn(MainController.class)
                                .oneGuess(guess.getId()))
                        .withSelfRel(),
                linkTo(
                        methodOn(MainController.class)
                                .main())
                        .withRel("/"));
    }
}