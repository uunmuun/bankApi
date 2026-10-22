package com.example.bankapi.dto;

import com.example.bankapi.entity.CardStatus;
import com.example.bankapi.entity.CardType;

import java.time.LocalDate;

public class CardResponse {

    private Long id;
    private String cardNumber;
    private LocalDate expirationDate;
    private CardType type;
    private CardStatus status;
    private Long accountId;

    public CardResponse(
            Long id,
            String cardNumber,
            LocalDate expirationDate,
            CardType type,
            CardStatus status,
            Long accountId
    ) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.type = type;
        this.status = status;
        this.accountId = accountId;
    }

    public Long getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public CardType getType() {
        return type;
    }

    public CardStatus getStatus() {
        return status;
    }

    public Long getAccountId() {
        return accountId;
    }
}