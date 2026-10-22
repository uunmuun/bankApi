package com.example.bankapi.exception;

public class PhoneNotFoundException extends RuntimeException {

    public PhoneNotFoundException(String message) {
        super(message);
    }
}