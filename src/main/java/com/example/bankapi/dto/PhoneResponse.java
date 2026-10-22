package com.example.bankapi.dto;

import com.example.bankapi.entity.PhoneType;

public class PhoneResponse {

    private Long id;
    private String number;
    private PhoneType type;

    public PhoneResponse(Long id, String number, PhoneType type) {
        this.id = id;
        this.number = number;
        this.type = type;
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
}