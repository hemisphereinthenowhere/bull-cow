package com.example.bullcow.exceptions;

public class GuessNotFoundException extends RuntimeException {

    public GuessNotFoundException(final String msg) {

        super(msg);
    }
}
