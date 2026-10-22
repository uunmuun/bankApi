package com.example.bankapi.dto;

import com.example.bankapi.entity.Currency;

import java.math.BigDecimal;

public class AccountResponse {

    private Long id;
    private String accountNumber;
    private Currency currency;
    private BigDecimal balance;
    private Long clientId;

    public AccountResponse(
            Long id,
            String accountNumber,
            Currency currency,
            BigDecimal balance,
            Long clientId
    ) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = balance;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Long getClientId() {
        return clientId;
    }
}