package com.example.bankapi.repository;

import com.example.bankapi.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
    List<Phone> findByClientId(Long clientId);
}