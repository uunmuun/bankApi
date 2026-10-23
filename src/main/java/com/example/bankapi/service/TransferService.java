package com.example.bankapi.service;

import com.example.bankapi.dto.TransferRequest;
import com.example.bankapi.entity.Account;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.CurrencyMismatchException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transfer(TransferRequest request) {

        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account with id " + request.getFromAccountId() + " not found"
                ));

        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account with id " + request.getToAccountId() + " not found"
                ));

        if (fromAccount.getId().equals(toAccount.getId())) {
            throw new IllegalArgumentException(
                    "Cannot transfer money to the same account"
            );
        }

        if (fromAccount.getCurrency() != toAccount.getCurrency()) {
            throw new CurrencyMismatchException(
                    "Accounts must have the same currency"
            );
        }

        BigDecimal amount = request.getAmount();

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds in account"
            );
        }

        fromAccount.setBalance(
                fromAccount.getBalance().subtract(amount)
        );

        toAccount.setBalance(
                toAccount.getBalance().add(amount)
        );

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}