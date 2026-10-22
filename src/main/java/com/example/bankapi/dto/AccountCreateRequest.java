package com.example.bankapi.dto;

import com.example.bankapi.entity.Currency;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class AccountCreateRequest {

    @NotNull
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