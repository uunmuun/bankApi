package com.example.bankapi.service;

import com.example.bankapi.dto.AccountCreateRequest;
import com.example.bankapi.dto.AccountResponse;
import com.example.bankapi.entity.Account;
import com.example.bankapi.entity.Client;
import com.example.bankapi.entity.Currency;
import com.example.bankapi.exception.ClientNotFoundException;
import com.example.bankapi.repository.AccountRepository;
import com.example.bankapi.repository.ClientRepository;
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
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void shouldCreateAccount() {

        Client client = new Client(
                "Ivanov",
                "Ivan",
                "Ivanovich",
                "ivan@example.com",
                LocalDate.of(1999, 5, 12)
        );

        AccountCreateRequest request = new AccountCreateRequest();
        request.setCurrency(Currency.KGS);
        request.setBalance(new BigDecimal("1000.00"));

        when(clientRepository.findById(1L))
                .thenReturn(Optional.of(client));

        Account account = new Account(
                null,
                Currency.KGS,
                new BigDecimal("1000.00"),
                client
        );

        when(accountRepository.save(any(Account.class)))
                .thenReturn(account);

        AccountResponse response = accountService.create(1L, request);

        assertNotNull(response);
        assertEquals(Currency.KGS, response.getCurrency());
        assertEquals(new BigDecimal("1000.00"), response.getBalance());

        verify(clientRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldNotCreateAccountWhenClientNotFound() {

        AccountCreateRequest request = new AccountCreateRequest();
        request.setCurrency(Currency.KGS);
        request.setBalance(new BigDecimal("1000.00"));

        when(clientRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFoundException.class,
                () -> accountService.create(999L, request)
        );

        verify(clientRepository).findById(999L);
        verify(accountRepository, never()).save(any(Account.class));
    }
    @Test
    void getById_shouldReturnAccount() {

        Client client = new Client(
                "Ivanov",
                "Ivan",
                "Ivanovich",
                "ivan@example.com",
                LocalDate.of(1999, 5, 12)
        );

        Account account = new Account(
                "123456789",
                Currency.KGS,
                new BigDecimal("1000.00"),
                client
        );

        ReflectionTestUtils.setField(account, "id", 1L);

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        AccountResponse response = accountService.getById(1L);

        assertEquals(1L, response.getId());
        assertEquals("123456789", response.getAccountNumber());
        assertEquals(Currency.KGS, response.getCurrency());
        assertEquals(new BigDecimal("1000.00"), response.getBalance());

        verify(accountRepository).findById(1L);
    }
}