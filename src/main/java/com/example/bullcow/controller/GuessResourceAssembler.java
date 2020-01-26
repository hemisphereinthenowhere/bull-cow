package com.example.bullcow.controller;

import com.example.bullcow.domain.Guess;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class GuessResourceAssembler implements ResourceAssembler<Guess, Resource<Guess>> {


    @Override
    public Resource<Guess> toResource(Guess guess) {

        return new Resource<>(guess,
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