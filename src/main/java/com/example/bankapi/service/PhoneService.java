package com.example.bankapi.service;

import com.example.bankapi.dto.PhoneCreateRequest;
import com.example.bankapi.dto.PhoneResponse;
import com.example.bankapi.entity.Client;
import com.example.bankapi.entity.Phone;
import com.example.bankapi.exception.ClientNotFoundException;
import com.example.bankapi.repository.ClientRepository;
import com.example.bankapi.repository.PhoneRepository;
import org.springframework.stereotype.Service;

@Service
public class PhoneService {

    private final PhoneRepository phoneRepository;
    private final ClientRepository clientRepository;

    public PhoneService(
            PhoneRepository phoneRepository,
            ClientRepository clientRepository
    ) {
        this.phoneRepository = phoneRepository;
        this.clientRepository = clientRepository;
    }

    public PhoneResponse create(Long clientId, PhoneCreateRequest request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new ClientNotFoundException(
                                "Client with id " + clientId + " not found"
                        )
                );

        Phone phone = new Phone(
                request.getNumber(),
                request.getType(),
                client
        );

        Phone savedPhone = phoneRepository.save(phone);

        return new PhoneResponse(
                savedPhone.getId(),
                savedPhone.getNumber(),
                savedPhone.getType()
        );
    }
}