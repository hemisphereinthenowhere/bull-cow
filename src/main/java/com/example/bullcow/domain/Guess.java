package com.example.bullcow.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
public class Guess {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Size (min = 4, max = 4, message = "Select 4 numbers!")
    private String selectedGuess;

    private String guessResult;
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @PrePersist
    void guessedAt() {
        this.date = new Date();
    }
}
