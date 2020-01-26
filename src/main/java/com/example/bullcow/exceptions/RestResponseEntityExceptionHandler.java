package com.example.bullcow.exceptions;

import com.example.bullcow.modules.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messages;

    @ExceptionHandler({ UserAlreadyExistException.class })
    public ResponseEntity<Object> handleUserAlreadyExist(RuntimeException ex, WebRequest request) {
        logger.error("409 Status Code", ex);

        return handleExceptionInternal(
                ex, "User already exists!", new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({ GuessNotFoundException.class })
    public ResponseEntity<Object> handleGuessNotFound(RuntimeException ex, WebRequest request) {
        logger.error("404 Status Code", ex);

        return handleExceptionInternal(
                ex, "GuessNotFound", new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }


    protected ResponseEntity<Object> handleBindException
            (BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("400 Status Code", ex);
        BindingResult result = ex.getBindingResult();
        GenericResponse bodyOfResponse =
                new GenericResponse(result.getFieldErrors(), result.getGlobalErrors());

        return handleExceptionInternal(
                ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


}
