package com.KamyEsm.AAA.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtGenerationException.class)
    public ResponseEntity<ErrorResponse> handleJwtGenerationException(JwtGenerationException ex) {
        return new ResponseEntity<>(new ErrorResponse(401 , "Failed to generate JWT token" , ex.getMessage() , LocalDateTime.now()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(500 , "Internal Server Error" , ex.getMessage() , LocalDateTime.now()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(404 , "Failed to find" , ex.getMessage() , LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(PasswordSecureException.class)
    public ResponseEntity<ErrorResponse> handlePasswordSecureException(PasswordSecureException e) {
        return new ResponseEntity<>(new ErrorResponse(403 , "Password is not secure." , e.getMessage() , LocalDateTime.now()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateNameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserNameException(DuplicateNameException e) {
        return new ResponseEntity<>(new ErrorResponse(403 , "name is already exist" , e.getMessage() , LocalDateTime.now()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        return new ResponseEntity<>(new ErrorResponse(403, "validation failed" , e.getMessage() , LocalDateTime.now() ), HttpStatus.FORBIDDEN );
    }



}
