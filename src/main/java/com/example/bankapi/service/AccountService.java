package com.example.bankapi.service;

import com.example.bankapi.dto.AccountCreateRequest;
import com.example.bankapi.dto.AccountResponse;
import com.example.bankapi.dto.AccountUpdateRequest;
import com.example.bankapi.entity.Account;
import com.example.bankapi.entity.Client;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.ClientNotFoundException;
import com.example.bankapi.repository.AccountRepository;
import com.example.bankapi.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public AccountResponse getById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account with id " + id + " not found"
                        )
                );

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getCurrency(),
                account.getBalance(),
                account.getClient().getId()
        );
    }
    public List<AccountResponse> getAll() {

        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountResponse(
                        account.getId(),
                        account.getAccountNumber(),
                        account.getCurrency(),
                        account.getBalance(),
                        account.getClient().getId()
                ))
                .toList();
    }
    public AccountResponse update(Long id, AccountUpdateRequest request) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account with id " + id + " not found"
                        )
                );

        account.setCurrency(request.getCurrency());
        account.setBalance(request.getBalance());

        Account updatedAccount = accountRepository.save(account);

        return new AccountResponse(
                updatedAccount.getId(),
                updatedAccount.getAccountNumber(),
                updatedAccount.getCurrency(),
                updatedAccount.getBalance(),
                updatedAccount.getClient().getId()
        );
    }
    public void delete(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account with id " + id + " not found"
                        )
                );

        accountRepository.delete(account);
    }
}