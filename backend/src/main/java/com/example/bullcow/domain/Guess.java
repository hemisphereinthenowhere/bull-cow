package com.example.bullcow.domain;

import com.example.bullcow.validations.UniqueDigits;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
public class Guess {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Digits(integer = 4, fraction = 0, message = "Select 4 numbers!")
    @NotEmpty
    @Size(min = 4, max = 4, message = "Select 4 numbers!")
    @UniqueDigits
    private String selectedGuess;

    private String guessResult;

    @JsonIgnore
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @PrePersist
    void guessedAt() {
        this.date = new Date();
    }
}
