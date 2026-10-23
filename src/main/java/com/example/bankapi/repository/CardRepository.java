package com.example.bankapi.repository;

import com.example.bankapi.entity.Card;
import com.example.bankapi.entity.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByAccountIdAndStatus(Long accountId, CardStatus status);

    void deleteByAccountId(Long accountId);

    List<Card> findByAccountId(Long accountId);
}