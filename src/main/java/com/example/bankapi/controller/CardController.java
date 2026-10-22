package com.example.bankapi.controller;

import com.example.bankapi.dto.CardCreateRequest;
import com.example.bankapi.dto.CardResponse;
import com.example.bankapi.dto.CardUpdateRequest;
import com.example.bankapi.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CardResponse>> getAll() {
        return ResponseEntity.ok(cardService.getAll());
    }

    @PostMapping
    public ResponseEntity<CardResponse> create(
            @RequestBody CardCreateRequest request
    ) {
        return ResponseEntity.ok(cardService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> update(
            @PathVariable Long id,
            @RequestBody CardUpdateRequest request
    ) {
        return ResponseEntity.ok(cardService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}