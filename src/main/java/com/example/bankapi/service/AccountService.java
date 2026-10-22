package com.example.bankapi.service;

import com.example.bankapi.dto.AccountCreateRequest;
import com.example.bankapi.dto.AccountResponse;
import com.example.bankapi.entity.Account;
import com.example.bankapi.entity.Client;
import com.example.bankapi.exception.ClientNotFoundException;
import com.example.bankapi.repository.AccountRepository;
import com.example.bankapi.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public AccountService(
            AccountRepository accountRepository,
            ClientRepository clientRepository
    ) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    public AccountResponse create(Long clientId, AccountCreateRequest request) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new ClientNotFoundException(
                                "Client with id " + clientId + " not found"
                        )
                );

        Account account = new Account(
                null,
                request.getCurrency(),
                request.getBalance(),
                client
        );

        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getAccountNumber(),
                savedAccount.getCurrency(),
                savedAccount.getBalance(),
                savedAccount.getClient().getId()
        );
    }
}