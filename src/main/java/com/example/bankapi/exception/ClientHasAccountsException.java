package com.example.bankapi.exception;

public class ClientHasAccountsException extends RuntimeException {

    public ClientHasAccountsException(String message) {
        super(message);
    }
}