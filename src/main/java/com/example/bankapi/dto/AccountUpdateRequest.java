package com.example.bankapi.dto;

import com.example.bankapi.entity.Currency;

import java.math.BigDecimal;

public class AccountUpdateRequest {

    private Currency currency;
    private BigDecimal balance;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}