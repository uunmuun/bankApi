package com.example.bankapi.controller;

import com.example.bankapi.dto.AccountCreateRequest;
import com.example.bankapi.dto.AccountResponse;
import com.example.bankapi.dto.AccountUpdateRequest;
import com.example.bankapi.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAll() {
        return ResponseEntity.ok(accountService.getAll());
    }
    @PostMapping("/client/{clientId}")
    public ResponseEntity<AccountResponse> create(
            @PathVariable Long clientId,
            @RequestBody AccountCreateRequest request
    ) {
        return ResponseEntity.ok(accountService.create(clientId, request));
    }
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> update(
            @PathVariable Long id,
            @RequestBody AccountUpdateRequest request
    ) {
        return ResponseEntity.ok(accountService.update(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AccountResponse>> getByClientId(
            @PathVariable Long clientId
    ) {
        return ResponseEntity.ok(accountService.getByClientId(clientId));
    }

}