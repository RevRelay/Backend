package com.revature.RevRelay.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handles exceptions for register and login
 */
@ControllerAdvice
public class ExceptionController {

    /**
     * Handles the UsernameNotFoundException by setting the Status and the message.
     *
     * @param e The UsernameNotFoundException being passed.
     * @return The Response Enity with the correct status and body for a UsernameNotFoundException.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    /**
     * Handles the AccessDeniedException by setting the Status and the message.
     *
     * @param e The AccessDeniedException being passed.
     * @return The Response Enity with the correct status and body for a AccessDeniedException.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
