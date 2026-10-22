package com.example.bankapi.dto;

import com.example.bankapi.entity.Currency;
import jakarta.validation.constraints.NotNull;

public class AccountUpdateRequest {

    @NotNull
    private Currency currency;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}