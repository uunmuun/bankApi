package com.example.bankapi.entity;

import jakarta.persistence.*;

@Entity
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    @Enumerated(EnumType.STRING)
    private PhoneType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    protected Phone() {
    }

    public Phone(String number, PhoneType type, Client client) {
        this.number = number;
        this.type = type;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public PhoneType getType() {
        return type;
    }

    public Client getClient() {
        return client;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setType(PhoneType type) {
        this.type = type;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}