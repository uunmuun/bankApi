package com.example.bankapi.service;

import com.example.bankapi.dto.ClientCreateRequest;
import com.example.bankapi.dto.ClientResponse;
import com.example.bankapi.dto.ClientUpdateRequest;
import com.example.bankapi.entity.Client;
import com.example.bankapi.exception.ClientAlreadyExistsException;
import com.example.bankapi.exception.ClientNotFoundException;
import com.example.bankapi.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientResponse create(ClientCreateRequest request) {
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

        Client savedClient = clientRepository.save(client);

        return new ClientResponse(
                savedClient.getId(),
                savedClient.getLastName(),
                savedClient.getFirstName(),
                savedClient.getMiddleName(),
                savedClient.getEmail(),
                savedClient.getBirthDate()
        );
    }

    public List<ClientResponse> getAll() {
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientResponse(
                        client.getId(),
                        client.getLastName(),
                        client.getFirstName(),
                        client.getMiddleName(),
                        client.getEmail(),
                        client.getBirthDate()
                ))
                .toList();
    }

    public ClientResponse getById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ClientNotFoundException(
                                "Client with id " + id + " not found"
                        )
                );

        return new ClientResponse(
                client.getId(),
                client.getLastName(),
                client.getFirstName(),
                client.getMiddleName(),
                client.getEmail(),
                client.getBirthDate()
        );
    }
    public ClientResponse update(Long id, ClientUpdateRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new ClientNotFoundException(
                                "Client with id " + id + " not found"
                        )
                );

        if (!client.getEmail().equals(request.getEmail())
                && clientRepository.existsByEmail(request.getEmail())) {
            throw new ClientAlreadyExistsException(
                    "Client with this email already exists"
            );
        }

        client.setLastName(request.getLastName());
        client.setFirstName(request.getFirstName());
        client.setMiddleName(request.getMiddleName());
        client.setEmail(request.getEmail());
        client.setBirthDate(request.getBirthDate());

        Client updatedClient = clientRepository.save(client);

        return new ClientResponse(
                updatedClient.getId(),
                updatedClient.getLastName(),
                updatedClient.getFirstName(),
                updatedClient.getMiddleName(),
                updatedClient.getEmail(),
                updatedClient.getBirthDate()
        );
    }
}