package com.example.bankapi.service;

import com.example.bankapi.dto.ClientCreateRequest;
import com.example.bankapi.dto.ClientResponse;
import com.example.bankapi.entity.Client;
import com.example.bankapi.exception.ClientAlreadyExistsException;
import com.example.bankapi.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldCreateClient() {
        ClientCreateRequest request = new ClientCreateRequest();
        request.setLastName("Ivanov");
        request.setFirstName("Ivan");
        request.setMiddleName("Ivanovich");
        request.setEmail("ivan@example.com");
        request.setBirthDate(LocalDate.of(1999, 5, 12));

        when(clientRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        Client client = new Client(
                request.getLastName(),
                request.getFirstName(),
                request.getMiddleName(),
                request.getEmail(),
                request.getBirthDate()
        );

        when(clientRepository.save(any(Client.class)))
                .thenReturn(client);

        ClientResponse result = clientService.create(request);

        assertNotNull(result);
        assertEquals("ivan@example.com", result.getEmail());

        verify(clientRepository).existsByEmail(request.getEmail());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void shouldNotCreateClientWithExistingEmail() {
        ClientCreateRequest request = new ClientCreateRequest();
        request.setLastName("Ivanov");
        request.setFirstName("Ivan");
        request.setEmail("ivan@example.com");
        request.setBirthDate(LocalDate.of(1999, 5, 12));

        when(clientRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThrows(
                ClientAlreadyExistsException.class,
                () -> clientService.create(request)
        );

        verify(clientRepository).existsByEmail(request.getEmail());
        verify(clientRepository, never()).save(any(Client.class));
    }
}