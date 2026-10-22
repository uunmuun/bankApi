package com.example.bankapi.dto;

import com.example.bankapi.entity.CardType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CardCreateRequest {

    @NotNull
    private Long accountId;

    @NotNull
    private LocalDate expirationDate;

    @NotNull
    private CardType type;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }
}