package com.example.bankapi.service;

import com.example.bankapi.dto.CardCreateRequest;
import com.example.bankapi.dto.CardResponse;
import com.example.bankapi.dto.CardUpdateRequest;
import com.example.bankapi.entity.Account;
import com.example.bankapi.entity.Card;
import com.example.bankapi.entity.CardStatus;
import com.example.bankapi.entity.CardType;
import com.example.bankapi.entity.Currency;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.CardNotFoundException;
import com.example.bankapi.repository.AccountRepository;
import com.example.bankapi.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CardService cardService;

    @Test
    void create_shouldCreateCard() {

        Account account = new Account(
                "123456789",
                Currency.KGS,
                new BigDecimal("1000.00"),
                null
        );

        ReflectionTestUtils.setField(account, "id", 1L);

        CardCreateRequest request = new CardCreateRequest();
        request.setAccountId(1L);
        request.setExpirationDate(LocalDate.of(2030, 10, 23));
        request.setType(CardType.DEBIT);

        Card card = new Card(
                "1234567890123456",
                LocalDate.of(2030, 10, 23),
                CardType.DEBIT,
                CardStatus.ACTIVE,
                account
        );

        ReflectionTestUtils.setField(card, "id", 1L);

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        when(cardRepository.save(any(Card.class)))
                .thenReturn(card);

        CardResponse response = cardService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("1234567890123456", response.getCardNumber());
        assertEquals(LocalDate.of(2030, 10, 23), response.getExpirationDate());
        assertEquals(CardType.DEBIT, response.getType());
        assertEquals(CardStatus.ACTIVE, response.getStatus());
        assertEquals(1L, response.getAccountId());

        verify(accountRepository).findById(1L);
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void create_shouldThrowExceptionWhenAccountNotFound() {

        CardCreateRequest request = new CardCreateRequest();
        request.setAccountId(999L);
        request.setExpirationDate(LocalDate.of(2030, 10, 23));
        request.setType(CardType.DEBIT);

        when(accountRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> cardService.create(request)
        );

        verify(accountRepository).findById(999L);
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void getById_shouldReturnCard() {

        Account account = new Account(
                "123456789",
                Currency.KGS,
                new BigDecimal("1000.00"),
                null
        );

        ReflectionTestUtils.setField(account, "id", 1L);

        Card card = new Card(
                "1234567890123456",
                LocalDate.of(2030, 10, 23),
                CardType.DEBIT,
                CardStatus.ACTIVE,
                account
        );

        ReflectionTestUtils.setField(card, "id", 1L);

        when(cardRepository.findById(1L))
                .thenReturn(Optional.of(card));

        CardResponse response = cardService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("1234567890123456", response.getCardNumber());
        assertEquals(LocalDate.of(2030, 10, 23), response.getExpirationDate());
        assertEquals(CardType.DEBIT, response.getType());
        assertEquals(CardStatus.ACTIVE, response.getStatus());
        assertEquals(1L, response.getAccountId());

        verify(cardRepository).findById(1L);
    }

    @Test
    void getById_shouldThrowExceptionWhenCardNotFound() {

        when(cardRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                CardNotFoundException.class,
                () -> cardService.getById(999L)
        );

        verify(cardRepository).findById(999L);
    }

    @Test
    void getAll_shouldReturnCards() {

        Account account = new Account(
                "123456789",
                Currency.KGS,
                new BigDecimal("1000.00"),
                null
        );

        ReflectionTestUtils.setField(account, "id", 1L);

        Card card1 = new Card(
                "1234567890123456",
                LocalDate.of(2030, 10, 23),
                CardType.DEBIT,
                CardStatus.ACTIVE,
                account
        );

        Card card2 = new Card(
                "9876543210987654",
                LocalDate.of(2031, 10, 23),
                CardType.CREDIT,
                CardStatus.ACTIVE,
                account
        );

        ReflectionTestUtils.setField(card1, "id", 1L);
        ReflectionTestUtils.setField(card2, "id", 2L);

        when(cardRepository.findAll())
                .thenReturn(List.of(card1, card2));

        List<CardResponse> responses = cardService.getAll();

        assertEquals(2, responses.size());

        assertEquals(1L, responses.get(0).getId());
        assertEquals("1234567890123456", responses.get(0).getCardNumber());
        assertEquals(LocalDate.of(2030, 10, 23), responses.get(0).getExpirationDate());
        assertEquals(CardType.DEBIT, responses.get(0).getType());
        assertEquals(CardStatus.ACTIVE, responses.get(0).getStatus());
        assertEquals(1L, responses.get(0).getAccountId());

        assertEquals(2L, responses.get(1).getId());
        assertEquals("9876543210987654", responses.get(1).getCardNumber());
        assertEquals(LocalDate.of(2031, 10, 23), responses.get(1).getExpirationDate());
        assertEquals(CardType.CREDIT, responses.get(1).getType());
        assertEquals(CardStatus.ACTIVE, responses.get(1).getStatus());
        assertEquals(1L, responses.get(1).getAccountId());

        verify(cardRepository).findAll();
    }

    @Test
    void update_shouldUpdateCard() {

        Account account = new Account(
                "123456789",
                Currency.KGS,
                new BigDecimal("1000.00"),
                null
        );

        ReflectionTestUtils.setField(account, "id", 1L);

        Card card = new Card(
                "1234567890123456",
                LocalDate.of(2030, 10, 23),
                CardType.DEBIT,
                CardStatus.ACTIVE,
                account
        );

        ReflectionTestUtils.setField(card, "id", 1L);

        CardUpdateRequest request = new CardUpdateRequest();
        request.setExpirationDate(LocalDate.of(2032, 12, 31));
        request.setType(CardType.CREDIT);
        request.setStatus(CardStatus.BLOCKED);

        when(cardRepository.findById(1L))
                .thenReturn(Optional.of(card));

        when(cardRepository.save(card))
                .thenReturn(card);

        CardResponse response = cardService.update(1L, request);

        assertEquals(1L, response.getId());
        assertEquals("1234567890123456", response.getCardNumber());
        assertEquals(LocalDate.of(2032, 12, 31), response.getExpirationDate());
        assertEquals(CardType.CREDIT, response.getType());
        assertEquals(CardStatus.BLOCKED, response.getStatus());
        assertEquals(1L, response.getAccountId());

        verify(cardRepository).findById(1L);
        verify(cardRepository).save(card);
    }

    @Test
    void delete_shouldDeleteCard() {

        Account account = new Account(
                "123456789",
                Currency.KGS,
                new BigDecimal("1000.00"),
                null
        );

        ReflectionTestUtils.setField(account, "id", 1L);

        Card card = new Card(
                "1234567890123456",
                LocalDate.of(2030, 10, 23),
                CardType.DEBIT,
                CardStatus.ACTIVE,
                account
        );

        ReflectionTestUtils.setField(card, "id", 1L);

        when(cardRepository.findById(1L))
                .thenReturn(Optional.of(card));

        cardService.delete(1L);

        verify(cardRepository).findById(1L);
        verify(cardRepository).delete(card);
    }

    @Test
    void delete_shouldThrowExceptionWhenCardNotFound() {

        when(cardRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                CardNotFoundException.class,
                () -> cardService.delete(999L)
        );

        verify(cardRepository).findById(999L);
        verify(cardRepository, never()).delete(any(Card.class));
    }

    @Test
    void getByAccountId_shouldReturnCardsOfAccount() {

        Account account = new Account(
                "123456789",
                Currency.KGS,
                new BigDecimal("1000.00"),
                null
        );

        ReflectionTestUtils.setField(account, "id", 1L);

        Card card = new Card(
                "1234567890123456",
                LocalDate.of(2030, 10, 23),
                CardType.DEBIT,
                CardStatus.ACTIVE,
                account
        );

        ReflectionTestUtils.setField(card, "id", 1L);

        when(accountRepository.existsById(1L))
                .thenReturn(true);
        when(cardRepository.findByAccountId(1L))
                .thenReturn(List.of(card));

        List<CardResponse> responses = cardService.getByAccountId(1L);

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals(1L, responses.get(0).getAccountId());

        verify(cardRepository).findByAccountId(1L);
    }

    @Test
    void getByAccountId_shouldThrowExceptionWhenAccountNotFound() {

        when(accountRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                AccountNotFoundException.class,
                () -> cardService.getByAccountId(999L)
        );

        verify(cardRepository, never()).findByAccountId(anyLong());
    }
}