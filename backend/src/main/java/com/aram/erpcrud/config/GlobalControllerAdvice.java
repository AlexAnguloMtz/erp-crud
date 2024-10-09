package com.aram.erpcrud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
class GlobalControllerAdvice {

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<String> handle(ResponseStatusException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getStatusCode());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<String> handle(DataIntegrityViolationException exception) {
        return new ResponseEntity<>("Data integrity violation", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> handle(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>("Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}