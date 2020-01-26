package com.example.bullcow.repos;

import com.example.bullcow.domain.Guess;
import com.example.bullcow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuessRepo extends JpaRepository<Guess, Long> {

    List<Guess> findByUserOrderByDateDesc(User user);

    long countByUser(User user);

    long countByUserAndGuessResult(User user, String guessResult);

}

