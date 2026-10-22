package com.example.bankapi.service;

import com.example.bankapi.dto.AccountCreateRequest;
import com.example.bankapi.dto.AccountResponse;
import com.example.bankapi.entity.Account;
import com.example.bankapi.entity.Client;
import com.example.bankapi.entity.Currency;
import com.example.bankapi.repository.AccountRepository;
import com.example.bankapi.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}