package com.example.bankapi.service;

import com.example.bankapi.dto.CardCreateRequest;
import com.example.bankapi.dto.CardResponse;
import com.example.bankapi.dto.CardUpdateRequest;
import com.example.bankapi.entity.Account;
import com.example.bankapi.entity.Card;
import com.example.bankapi.entity.CardStatus;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.CardNotFoundException;
import com.example.bankapi.repository.AccountRepository;
import com.example.bankapi.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public CardService(
            CardRepository cardRepository,
            AccountRepository accountRepository
    ) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }

    public CardResponse create(CardCreateRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account with id " + request.getAccountId() + " not found"
                        )
                );

        Card card = new Card(
                generateCardNumber(),
                request.getExpirationDate(),
                request.getType(),
                CardStatus.ACTIVE,
                account
        );

        Card savedCard = cardRepository.save(card);

        return new CardResponse(
                savedCard.getId(),
                savedCard.getCardNumber(),
                savedCard.getExpirationDate(),
                savedCard.getType(),
                savedCard.getStatus(),
                savedCard.getAccount().getId()
        );
    }

    public CardResponse getById(Long id) {

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new CardNotFoundException(
                                "Card with id " + id + " not found"
                        )
                );

        return new CardResponse(
                card.getId(),
                card.getCardNumber(),
                card.getExpirationDate(),
                card.getType(),
                card.getStatus(),
                card.getAccount().getId()
        );
    }

    public List<CardResponse> getAll() {

        return cardRepository.findAll()
                .stream()
                .map(card -> new CardResponse(
                        card.getId(),
                        card.getCardNumber(),
                        card.getExpirationDate(),
                        card.getType(),
                        card.getStatus(),
                        card.getAccount().getId()
                ))
                .toList();
    }

    public CardResponse update(Long id, CardUpdateRequest request) {

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new CardNotFoundException(
                                "Card with id " + id + " not found"
                        )
                );

        card.setExpirationDate(request.getExpirationDate());
        card.setType(request.getType());
        card.setStatus(request.getStatus());

        Card updatedCard = cardRepository.save(card);

        return new CardResponse(
                updatedCard.getId(),
                updatedCard.getCardNumber(),
                updatedCard.getExpirationDate(),
                updatedCard.getType(),
                updatedCard.getStatus(),
                updatedCard.getAccount().getId()
        );
    }

    public CardResponse block(Long id) {

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new CardNotFoundException(
                                "Card with id " + id + " not found"
                        )
                );

        card.setStatus(CardStatus.BLOCKED);

        Card blockedCard = cardRepository.save(card);

        return new CardResponse(
                blockedCard.getId(),
                blockedCard.getCardNumber(),
                blockedCard.getExpirationDate(),
                blockedCard.getType(),
                blockedCard.getStatus(),
                blockedCard.getAccount().getId()
        );
    }

    public CardResponse unblock(Long id) {

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new CardNotFoundException(
                                "Card with id " + id + " not found"
                        )
                );

        card.setStatus(CardStatus.ACTIVE);

        Card unblockedCard = cardRepository.save(card);

        return new CardResponse(
                unblockedCard.getId(),
                unblockedCard.getCardNumber(),
                unblockedCard.getExpirationDate(),
                unblockedCard.getType(),
                unblockedCard.getStatus(),
                unblockedCard.getAccount().getId()
        );
    }

    public void delete(Long id) {

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new CardNotFoundException(
                                "Card with id " + id + " not found"
                        )
                );

        cardRepository.delete(card);
    }

    private String generateCardNumber() {
        long number = 1000000000000000L
                + (long) (Math.random() * 9000000000000000L);

        return String.valueOf(number);
    }
}