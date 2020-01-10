package com.example.bullcow.controller;


import com.example.bullcow.domain.Guess;
import com.example.bullcow.domain.User;
import com.example.bullcow.modules.NumberProcessor;
import com.example.bullcow.repos.GuessRepo;
import com.example.bullcow.repos.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
public class MainController {

	@Autowired
	private GuessRepo guessRepo;

	@Autowired
	private UserRepo userRepo;

    @GetMapping("/main")
    public String main(Map<String, Object> model, Guess guess, @AuthenticationPrincipal User user) {


		Iterable<Guess> guesses = guessRepo.findByUserOrderByDateDesc(user);

		model.put("guesses", guesses);
    	return "main";
    }

    @PostMapping("/main")
	public String add(
			@AuthenticationPrincipal User user,
			@Valid Guess guess,
			BindingResult bindingResult,
			Map<String, Object> model) {

		if (bindingResult.hasErrors()) {
			return "main";
		}

    	String guessResult = NumberProcessor.verifyNumber(guess.getSelectedGuess(), user.getGeneratedNumber());

		if (guessResult.equals("4Б0К")) {
			user.setGeneratedNumber(NumberProcessor.generateNumber());
			userRepo.save(user);
		}

    	guess.setGuessResult(guessResult);
    	guess.setUser(user);

    	guessRepo.save(guess);

		Iterable<Guess> guesses = guessRepo.findByUserOrderByDateDesc(user);

		model.put("guesses", guesses);

    	return "main";
	}
}