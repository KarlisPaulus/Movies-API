package com.example.moviesapi.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.JsonMappingException;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

   
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        // Extract field errors and map them to error messages
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage(),
                        (existing, replacement) -> existing));

        return ResponseEntity.status(400).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolations(ConstraintViolationException ex) {
        // Extract violations and map them to error messages
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString()
                                .substring(violation.getPropertyPath().toString().lastIndexOf('.') + 1),
                        violation -> violation.getMessage(),
                        (existing, replacement) -> existing));

        return ResponseEntity.status(400).body(errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(500).body(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String errorMessage = "Invalid input format";
        Throwable cause = ex.getCause();

        if (cause instanceof JsonMappingException) {
            JsonMappingException jme = (JsonMappingException) cause;
            if (jme.getCause() instanceof DateTimeParseException) {
                DateTimeParseException dtpe = (DateTimeParseException) jme.getCause();
                errorMessage = "Invalid date format. Please use the format YYYY-MM-DD. Error: " + dtpe.getMessage();
            }
        }

        return ResponseEntity.status(400).body(Map.of("error", errorMessage));
    }

}