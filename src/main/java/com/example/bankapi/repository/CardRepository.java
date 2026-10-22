package com.example.bankapi.repository;

import com.example.bankapi.entity.Card;
import com.example.bankapi.entity.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByAccountIdAndStatus(Long accountId, CardStatus status);
}