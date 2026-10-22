package com.example.bankapi.exception;

public class AccountNotEmptyException extends RuntimeException {

    public AccountNotEmptyException(String message) {
        super(message);
    }
}