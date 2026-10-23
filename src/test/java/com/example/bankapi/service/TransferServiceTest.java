package com.example.bankapi.service;

import com.example.bankapi.dto.TransferRequest;
import com.example.bankapi.entity.Account;
import com.example.bankapi.entity.Client;
import com.example.bankapi.entity.Currency;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.CurrencyMismatchException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferService transferService;

    @Test
    void shouldTransferMoneyBetweenAccounts() {

        Client client = new Client(
                "Ivanov",
                "Ivan",
                "Ivanovich",
                "ivan@example.com",
                LocalDate.of(1999, 5, 12)
        );

        Account fromAccount = new Account(
                "123456789",
                Currency.KGS,
                new BigDecimal("1000.00"),
                client
        );

        Account toAccount = new Account(
                "987654321",
                Currency.KGS,
                new BigDecimal("500.00"),
                client
        );

        ReflectionTestUtils.setField(fromAccount, "id", 1L);
        ReflectionTestUtils.setField(toAccount, "id", 2L);

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(fromAccount));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(toAccount));

        TransferRequest request = new TransferRequest(
                1L,
                2L,
                new BigDecimal("300.00")
        );

        transferService.transfer(request);

        assertEquals(
                new BigDecimal("700.00"),
                fromAccount.getBalance()
        );

        assertEquals(
                new BigDecimal("800.00"),
                toAccount.getBalance()
        );

        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(2L);
        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
    }

    @Test
    void shouldThrowExceptionWhenFromAccountNotFound() {

        when(accountRepository.findById(999L))
                .thenReturn(Optional.empty());

        TransferRequest request = new TransferRequest(
                999L,
                2L,
                new BigDecimal("100.00")
        );

        assertThrows(
                AccountNotFoundException.class,
                () -> transferService.transfer(request)
        );

        verify(accountRepository).findById(999L);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenToAccountNotFound() {

        Account fromAccount = new Account(
                "123456789",
                Currency.KGS,
                new BigDecimal("1000.00"),
                null
        );

        ReflectionTestUtils.setField(fromAccount, "id", 1L);

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(fromAccount));

        when(accountRepository.findById(999L))
                .thenReturn(Optional.empty());

        TransferRequest request = new TransferRequest(
                1L,
                999L,
                new BigDecimal("100.00")
        );

        assertThrows(
                AccountNotFoundException.class,
                () -> transferService.transfer(request)
        );

        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(999L);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenCurrenciesAreDifferent() {

        Client client = new Client(
                "Ivanov",
                "Ivan",
                "Ivanovich",
                "ivan@example.com",
                LocalDate.of(1999, 5, 12)
        );

        Account fromAccount = new Account(
                "123456789",
                Currency.KGS,
                new BigDecimal("1000.00"),
                client
        );

        Account toAccount = new Account(
                "987654321",
                Currency.USD,
                new BigDecimal("500.00"),
                client
        );

        ReflectionTestUtils.setField(fromAccount, "id", 1L);
        ReflectionTestUtils.setField(toAccount, "id", 2L);

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(fromAccount));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(toAccount));

        TransferRequest request = new TransferRequest(
                1L,
                2L,
                new BigDecimal("100.00")
        );

        assertThrows(
                CurrencyMismatchException.class,
                () -> transferService.transfer(request)
        );

        assertEquals(
                new BigDecimal("1000.00"),
                fromAccount.getBalance()
        );

        assertEquals(
                new BigDecimal("500.00"),
                toAccount.getBalance()
        );

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {

        Client client = new Client(
                "Ivanov",
                "Ivan",
                "Ivanovich",
                "ivan@example.com",
                LocalDate.of(1999, 5, 12)
        );

        Account fromAccount = new Account(
                "123456789",
                Currency.KGS,
                new BigDecimal("100.00"),
                client
        );

        Account toAccount = new Account(
                "987654321",
                Currency.KGS,
                new BigDecimal("500.00"),
                client
        );

        ReflectionTestUtils.setField(fromAccount, "id", 1L);
        ReflectionTestUtils.setField(toAccount, "id", 2L);

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(fromAccount));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(toAccount));

        TransferRequest request = new TransferRequest(
                1L,
                2L,
                new BigDecimal("200.00")
        );

        assertThrows(
                InsufficientFundsException.class,
                () -> transferService.transfer(request)
        );

        assertEquals(
                new BigDecimal("100.00"),
                fromAccount.getBalance()
        );

        assertEquals(
                new BigDecimal("500.00"),
                toAccount.getBalance()
        );

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenTransferringToSameAccount() {

        Account account = new Account(
                "123456789",
                Currency.KGS,
                new BigDecimal("1000.00"),
                null
        );

        ReflectionTestUtils.setField(account, "id", 1L);

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        TransferRequest request = new TransferRequest(
                1L,
                1L,
                new BigDecimal("100.00")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> transferService.transfer(request)
        );

        assertEquals(
                new BigDecimal("1000.00"),
                account.getBalance()
        );

        verify(accountRepository, never()).save(any(Account.class));
    }
}