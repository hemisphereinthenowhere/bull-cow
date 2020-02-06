package com.example.bullcow.controller;


import com.example.bullcow.domain.Guess;
import com.example.bullcow.domain.User;
import com.example.bullcow.exceptions.GuessNotFoundException;
import com.example.bullcow.repos.GuessRepo;
import com.example.bullcow.repos.UserRepo;
import com.example.bullcow.service.NumberProcessor;
import com.example.bullcow.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/rest")
public class MainController {

	@Autowired
	private UserResourceAssembler userResourceAssembler;

	@Autowired
	private GuessResourceAssembler guessResourceAssembler;

	@Autowired
	private GuessRepo guessRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private UserService userService;


	@GetMapping("/users")
	CollectionModel<EntityModel<User>> allUsers() {

		List<EntityModel<User>> users = userRepo
				.findByRatingNotNullOrderByRating()
				.stream()
				.map(userResourceAssembler::toModel)
				.collect(Collectors.toList());

		return new CollectionModel<>(users,
				linkTo(methodOn(MainController.class).allUsers()).withSelfRel());
	}

	@GetMapping("/users/{id}")
	EntityModel<User> oneUser(@PathVariable Long id) {

		User user = userRepo.findById(id)
				.orElseThrow(() -> new AuthenticationServiceException("User not found"));

		return userResourceAssembler.toModel(user);
	}

    @GetMapping("/main")
	CollectionModel<EntityModel<Guess>> main() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String username = authentication.getName();
		User user = userRepo.findByUsername(username);

		List<EntityModel<Guess>> guesses = guessRepo
				.findByUserOrderByDateDesc(user)
				.stream()
				.map(guessResourceAssembler::toModel)
				.collect(Collectors.toList());

		return new CollectionModel<>(guesses,
				linkTo(methodOn(MainController.class).main()).withSelfRel());
    }

	@GetMapping("/main/{id}")
	EntityModel<Guess> oneGuess(@PathVariable Long id) {
		Guess guess = guessRepo.findById(id)
				.orElseThrow(() -> new GuessNotFoundException("Guess not found"));
		return guessResourceAssembler.toModel(guess);
	}







    @PostMapping("/main")
	ResponseEntity<EntityModel<Guess>> add(@RequestBody @Valid Guess guess) throws URISyntaxException {

		log.info("Selected guess:" + guess.getSelectedGuess());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


		User user = (User) userService.loadUserByUsername(authentication.getName());
    	String guessResult = NumberProcessor.verifyNumber(guess.getSelectedGuess(), user.getGeneratedNumber());

		if (guessResult.equals("4Б0К")) {
			user.setGeneratedNumber(NumberProcessor.generateNumber());
            log.info("On-success generated number:" + user.getGeneratedNumber());
			userRepo.save(user);
		}

    	guess.setGuessResult(guessResult);
    	guess.setUser(user);

		guessRepo.save(guess);

		long successCount = guessRepo.countByUserAndGuessResult(user, "4Б0К");
		if (successCount > 0) {
			double rating = (double) guessRepo.countByUser(user) / (double) successCount;
			rating = BigDecimal.valueOf(rating)
					.setScale(2, RoundingMode.HALF_UP)
					.doubleValue();

			user.setRating(rating);
			userRepo.save(user);
		}

		EntityModel<Guess> resource = guessResourceAssembler.toModel(guess);

		return new ResponseEntity<>(resource, HttpStatus.CREATED);
	}



}