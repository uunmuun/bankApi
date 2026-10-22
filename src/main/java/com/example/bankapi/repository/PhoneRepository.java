package com.example.bankapi.repository;

import com.example.bankapi.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
    List<Phone> findByClientId(Long clientId);
    Optional<Phone> findByIdAndClientId(Long id, Long clientId);
}