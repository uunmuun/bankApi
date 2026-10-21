package com.example.bankapi.service;

import com.example.bankapi.dto.ClientCreateRequest;
import com.example.bankapi.entity.Client;
import com.example.bankapi.exception.ClientAlreadyExistsException;
import com.example.bankapi.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    public Client create(ClientCreateRequest request) {
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new ClientAlreadyExistsException(
                    "Client with this email already exists"
            );
        }

        Client client = new Client(
                request.getLastName(),
                request.getFirstName(),
                request.getMiddleName(),
                request.getEmail(),
                request.getBirthDate()
        );

        return clientRepository.save(client);
    }
}