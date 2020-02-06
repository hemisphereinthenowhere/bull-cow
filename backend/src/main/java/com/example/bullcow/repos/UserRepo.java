package com.example.bullcow.repos;

import com.example.bullcow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByRatingNotNullOrderByRating();

}
