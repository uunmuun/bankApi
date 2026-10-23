package com.example.bankapi.exception;

public class CurrencyChangeNotAllowedException extends RuntimeException {

    public CurrencyChangeNotAllowedException(String message) {
        super(message);
    }
}