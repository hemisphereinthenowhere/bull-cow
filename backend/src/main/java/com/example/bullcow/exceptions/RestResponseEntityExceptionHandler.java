package com.example.bullcow.exceptions;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    private static final long serialVersionUID = -8970718410437077606L;

    @Autowired
    private MessageSource messages;

    @ExceptionHandler({ UserAlreadyExistException.class })
    public ResponseEntity<Object> handleUserAlreadyExist(RuntimeException ex, WebRequest request, HttpServletResponse response) throws IOException {

        logger.error("409 Status Code", ex);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("error", true);
        jsonResponse.put("message", "User already exist");

        return handleExceptionInternal(
                ex, jsonResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
    @ExceptionHandler({ UsernameNotFoundException.class })
    public ResponseEntity<Object> handleUserNotFound(RuntimeException ex, WebRequest request, HttpServletResponse response) {

        logger.error("404 Status Code", ex);

        return handleExceptionInternal(
                ex, "{\"message\":\"User already exist\"}", new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({ GuessNotFoundException.class })
    public ResponseEntity<Object> handleGuessNotFound(RuntimeException ex, WebRequest request) {
        logger.error("404 Status Code", ex);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("error", true);
        jsonResponse.put("message", "Username not found");

        return handleExceptionInternal(
                ex, jsonResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }


    protected ResponseEntity<Object> handleBindException
            (BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("400 Status Code", ex);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("error", true);
        jsonResponse.put("message", "Non-valid value");

        return handleExceptionInternal(
                ex, jsonResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


}
