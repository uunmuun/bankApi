package com.example.bankapi.dto;

import com.example.bankapi.entity.CardStatus;
import com.example.bankapi.entity.CardType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CardUpdateRequest {

    @NotNull
    private LocalDate expirationDate;

    @NotNull
    private CardType type;

    @NotNull
    private CardStatus status;

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

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }
}