package com.example.bankapi.controller;

import com.example.bankapi.dto.PhoneCreateRequest;
import com.example.bankapi.dto.PhoneResponse;
import com.example.bankapi.service.PhoneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients/{clientId}/phones")
public class PhoneController {

    private final PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PhoneResponse create(
            @PathVariable Long clientId,
            @Valid @RequestBody PhoneCreateRequest request
    ) {
        return phoneService.create(clientId, request);
    }
    @GetMapping
    public List<PhoneResponse> getAll(@PathVariable Long clientId) {
        return phoneService.getAll(clientId);
    }
    @GetMapping("/{phoneId}")
    public PhoneResponse getById(
            @PathVariable Long clientId,
            @PathVariable Long phoneId
    ) {
        return phoneService.getById(clientId, phoneId);
    }
}