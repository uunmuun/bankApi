package com.example.bankapi.service;

import com.example.bankapi.dto.TransferRequest;
import com.example.bankapi.entity.Account;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.CurrencyMismatchException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.exception.SameAccountTransferException;
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

        Long fromId = request.getFromAccountId();
        Long toId = request.getToAccountId();

        if (fromId.equals(toId)) {
            throw new SameAccountTransferException(
                    "Cannot transfer money to the same account"
            );
        }

        // Блокируем счета в порядке возрастания id,
        // чтобы встречные переводы не могли зайти в дедлок
        Long firstId = Math.min(fromId, toId);
        Long secondId = Math.max(fromId, toId);

        Account first = lockAccount(firstId);
        Account second = lockAccount(secondId);

        Account fromAccount = fromId.equals(firstId) ? first : second;
        Account toAccount = fromId.equals(firstId) ? second : first;

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

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    private Account lockAccount(Long id) {
        return accountRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account with id " + id + " not found"
                ));
    }
}