package com.example.bankapi.service;

import com.example.bankapi.dto.AccountCreateRequest;
import com.example.bankapi.dto.AccountResponse;
import com.example.bankapi.dto.AccountUpdateRequest;
import com.example.bankapi.entity.Account;
import com.example.bankapi.entity.CardStatus;
import com.example.bankapi.entity.Client;
import com.example.bankapi.exception.AccountNotEmptyException;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.ClientNotFoundException;
import com.example.bankapi.exception.CurrencyChangeNotAllowedException;
import com.example.bankapi.repository.AccountRepository;
import com.example.bankapi.repository.CardRepository;
import com.example.bankapi.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final CardRepository cardRepository;

    public AccountService(
            AccountRepository accountRepository,
            ClientRepository clientRepository,
            CardRepository cardRepository
    ) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.cardRepository = cardRepository;
    }

    public AccountResponse create(Long clientId, AccountCreateRequest request) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new ClientNotFoundException(
                                "Client with id " + clientId + " not found"
                        )
                );

        String accountNumber = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);

        Account account = new Account(
                accountNumber,
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

        if (account.getCurrency() != request.getCurrency()
                && account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new CurrencyChangeNotAllowedException(
                    "Cannot change currency of an account with non-zero balance"
            );
        }

        account.setCurrency(request.getCurrency());

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

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new AccountNotEmptyException(
                    "Account balance must be zero to close the account"
            );
        }

        if (cardRepository.existsByAccountIdAndStatus(id, CardStatus.ACTIVE)) {
            throw new AccountNotEmptyException(
                    "Account has active cards and cannot be closed"
            );
        }

        cardRepository.deleteByAccountId(id);
        accountRepository.delete(account);
    }

    public List<AccountResponse> getByClientId(Long clientId) {

        clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new ClientNotFoundException(
                                "Client with id " + clientId + " not found"
                        )
                );

        return accountRepository.findByClientId(clientId)
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
}