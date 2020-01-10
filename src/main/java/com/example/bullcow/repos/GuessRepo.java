package com.example.bullcow.repos;

import com.example.bullcow.domain.Guess;
import com.example.bullcow.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GuessRepo extends CrudRepository<Guess, Long> {

    List<Guess> findByUserOrderByDateDesc(User usr);
}
