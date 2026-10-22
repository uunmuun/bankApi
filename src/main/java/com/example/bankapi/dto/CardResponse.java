package com.example.bankapi.dto;

import com.example.bankapi.entity.CardStatus;

public class CardResponse {

    private Long id;
    private String cardNumber;
    private CardStatus status;
    private Long accountId;

    public CardResponse(
            Long id,
            String cardNumber,
            CardStatus status,
            Long accountId
    ) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.status = status;
        this.accountId = accountId;
    }

    public Long getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public CardStatus getStatus() {
        return status;
    }

    public Long getAccountId() {
        return accountId;
    }
}