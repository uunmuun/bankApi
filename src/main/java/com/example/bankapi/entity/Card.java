package com.example.bankapi.entity;

import jakarta.persistence.*;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardNumber;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    protected Card() {
    }

    public Card(
            String cardNumber,
            CardStatus status,
            Account account
    ) {
        this.cardNumber = cardNumber;
        this.status = status;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}