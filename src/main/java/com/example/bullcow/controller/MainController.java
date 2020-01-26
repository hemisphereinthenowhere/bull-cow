package com.example.bullcow.controller;


import com.example.bullcow.domain.Guess;
import com.example.bullcow.domain.User;
import com.example.bullcow.exceptions.GuessNotFoundException;
import com.example.bullcow.modules.NumberProcessor;
import com.example.bullcow.repos.GuessRepo;
import com.example.bullcow.repos.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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


	@GetMapping("/users")
	Resources<Resource<User>> allUsers() {

		List<Resource<User>> users = userRepo
				.findByRatingNotNullOrderByRatingDesc()
				.stream()
				.map(userResourceAssembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(users,
				linkTo(methodOn(MainController.class).allUsers()).withSelfRel());
	}

	@GetMapping("/users/{id}")
	Resource<User> oneUser(@PathVariable Long id) {

		User user = userRepo.findById(id)
				.orElseThrow(() -> new AuthenticationServiceException("User not found"));

		return userResourceAssembler.toResource(user);
	}

    @GetMapping("/main")
	Resources<Resource<Guess>> main() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String username = authentication.getName();
		User user = userRepo.findByUsername(username);

		List<Resource<Guess>> guesses = guessRepo
				.findByUserOrderByDateDesc(user)
				.stream()
				.map(guessResourceAssembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(guesses,
				linkTo(methodOn(MainController.class).main()).withSelfRel());
    }

	@GetMapping("/main/{id}")
	Resource<Guess> oneGuess(@PathVariable Long id) {
		Guess guess = guessRepo.findById(id)
				.orElseThrow(() -> new GuessNotFoundException("Guess not found"));
		return guessResourceAssembler.toResource(guess);
	}







    @PostMapping("/main")
	ResponseEntity<Resource<Guess>> add(@RequestBody @Valid Guess guess) throws URISyntaxException {

		log.info("Selected guess:" + guess.getSelectedGuess());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String username = authentication.getName();
		User user = userRepo.findByUsername(username);
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

		Resource<Guess> resource = guessResourceAssembler.toResource(guess);

		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}



}