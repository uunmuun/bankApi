package com.example.bankapi.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    protected Account() {
    }

    public Account(
            String accountNumber,
            Currency currency,
            BigDecimal balance,
            Client client
    ) {
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = balance;
        this.client = client;
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

    public Client getClient() {
        return client;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}