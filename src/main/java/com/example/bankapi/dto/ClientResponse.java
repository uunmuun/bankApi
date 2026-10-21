package com.example.bankapi.dto;

import java.time.LocalDate;

public class ClientResponse {

    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String email;
    private LocalDate birthDate;

    public ClientResponse(Long id, String lastName, String firstName,
                          String middleName, String email, LocalDate birthDate) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.email = email;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}