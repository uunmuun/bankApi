package com.example.bankapi.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardNumber;

    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    private CardType type;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    protected Card() {
    }

    public Card(
            String cardNumber,
            LocalDate expirationDate,
            CardType type,
            CardStatus status,
            Account account
    ) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.type = type;
        this.status = status;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}