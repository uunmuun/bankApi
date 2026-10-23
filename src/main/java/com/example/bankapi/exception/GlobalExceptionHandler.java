package com.example.bankapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleClientAlreadyExists(ClientAlreadyExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleClientNotFound(ClientNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(PhoneNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePhoneNotFound(PhoneNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAccountNotFound(AccountNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(AccountNotEmptyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleAccountNotEmpty(AccountNotEmptyException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(CardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCardNotFound(CardNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(CurrencyMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleCurrencyMismatch(CurrencyMismatchException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInsufficientFunds(InsufficientFundsException exception) {
        return exception.getMessage();
    }
}