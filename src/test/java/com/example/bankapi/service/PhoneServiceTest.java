package com.example.bankapi.service;

import com.example.bankapi.dto.PhoneCreateRequest;
import com.example.bankapi.dto.PhoneResponse;
import com.example.bankapi.dto.PhoneUpdateRequest;
import com.example.bankapi.entity.Client;
import com.example.bankapi.entity.Phone;
import com.example.bankapi.entity.PhoneType;
import com.example.bankapi.exception.ClientNotFoundException;
import com.example.bankapi.exception.PhoneNotFoundException;
import com.example.bankapi.repository.ClientRepository;
import com.example.bankapi.repository.PhoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhoneServiceTest {

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private PhoneService phoneService;

    @Test
    void create_shouldCreatePhone() {
        Client client = new Client(
                "Ivanov",
                "Ivan",
                "Ivanovich",
                "ivan@example.com",
                LocalDate.of(1999, 5, 12)
        );

        ReflectionTestUtils.setField(client, "id", 1L);

        PhoneCreateRequest request = new PhoneCreateRequest();
        request.setNumber("+996700123456");
        request.setType(PhoneType.MOBILE);

        when(clientRepository.findById(1L))
                .thenReturn(Optional.of(client));

        Phone phone = new Phone(
                request.getNumber(),
                request.getType(),
                client
        );

        ReflectionTestUtils.setField(phone, "id", 1L);

        when(phoneRepository.save(any(Phone.class)))
                .thenReturn(phone);

        PhoneResponse response = phoneService.create(1L, request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("+996700123456", response.getNumber());
        assertEquals(PhoneType.MOBILE, response.getType());

        verify(clientRepository).findById(1L);
        verify(phoneRepository).save(any(Phone.class));
    }

    @Test
    void create_shouldThrowExceptionWhenClientNotFound() {
        PhoneCreateRequest request = new PhoneCreateRequest();
        request.setNumber("+996700123456");
        request.setType(PhoneType.MOBILE);

        when(clientRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFoundException.class,
                () -> phoneService.create(999L, request)
        );

        verify(clientRepository).findById(999L);
        verify(phoneRepository, never()).save(any(Phone.class));
    }
    @Test
    void getAll_shouldReturnClientPhones() {
        Client client = new Client(
                "Ivanov",
                "Ivan",
                "Ivanovich",
                "ivan@example.com",
                LocalDate.of(1999, 5, 12)
        );

        ReflectionTestUtils.setField(client, "id", 1L);

        Phone phone = new Phone(
                "+996700123456",
                PhoneType.MOBILE,
                client
        );

        ReflectionTestUtils.setField(phone, "id", 1L);

        when(clientRepository.existsById(1L))
                .thenReturn(true);

        when(phoneRepository.findByClientId(1L))
                .thenReturn(List.of(phone));

        List<PhoneResponse> response = phoneService.getAll(1L);

        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getId());
        assertEquals("+996700123456", response.get(0).getNumber());
        assertEquals(PhoneType.MOBILE, response.get(0).getType());

        verify(clientRepository).existsById(1L);
        verify(phoneRepository).findByClientId(1L);
    }
    @Test
    void getAll_shouldThrowExceptionWhenClientNotFound() {
        when(clientRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                ClientNotFoundException.class,
                () -> phoneService.getAll(999L)
        );

        verify(clientRepository).existsById(999L);
        verify(phoneRepository, never()).findByClientId(anyLong());
    }
    @Test
    void getById_shouldReturnPhone() {
        Client client = new Client(
                "Ivanov",
                "Ivan",
                "Ivanovich",
                "ivan@example.com",
                LocalDate.of(1999, 5, 12)
        );

        ReflectionTestUtils.setField(client, "id", 1L);

        Phone phone = new Phone(
                "+996700123456",
                PhoneType.MOBILE,
                client
        );

        ReflectionTestUtils.setField(phone, "id", 5L);

        when(phoneRepository.findByIdAndClientId(5L, 1L))
                .thenReturn(Optional.of(phone));

        PhoneResponse response = phoneService.getById(1L, 5L);

        assertEquals(5L, response.getId());
        assertEquals("+996700123456", response.getNumber());
        assertEquals(PhoneType.MOBILE, response.getType());

        verify(phoneRepository).findByIdAndClientId(5L, 1L);
    }
    @Test
    void getById_shouldThrowExceptionWhenPhoneNotFound() {
        when(phoneRepository.findByIdAndClientId(999L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                PhoneNotFoundException.class,
                () -> phoneService.getById(1L, 999L)
        );

        verify(phoneRepository).findByIdAndClientId(999L, 1L);
    }
    @Test
    void update_shouldUpdatePhone() {
        Client client = new Client(
                "Ivanov",
                "Ivan",
                "Ivanovich",
                "ivan@example.com",
                LocalDate.of(1999, 5, 12)
        );

        ReflectionTestUtils.setField(client, "id", 1L);

        Phone phone = new Phone(
                "+996700123456",
                PhoneType.MOBILE,
                client
        );

        ReflectionTestUtils.setField(phone, "id", 5L);

        PhoneUpdateRequest request = new PhoneUpdateRequest();
        request.setNumber("+996555987654");
        request.setType(PhoneType.WORK);

        when(phoneRepository.findByIdAndClientId(5L, 1L))
                .thenReturn(Optional.of(phone));

        when(phoneRepository.save(phone))
                .thenReturn(phone);

        PhoneResponse response = phoneService.update(1L, 5L, request);

        assertEquals(5L, response.getId());
        assertEquals("+996555987654", response.getNumber());
        assertEquals(PhoneType.WORK, response.getType());

        verify(phoneRepository).findByIdAndClientId(5L, 1L);
        verify(phoneRepository).save(phone);
    }
    @Test
    void update_shouldThrowExceptionWhenPhoneNotFound() {
        PhoneUpdateRequest request = new PhoneUpdateRequest();
        request.setNumber("+996555987654");
        request.setType(PhoneType.WORK);

        when(phoneRepository.findByIdAndClientId(999L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(
                PhoneNotFoundException.class,
                () -> phoneService.update(1L, 999L, request)
        );

        verify(phoneRepository).findByIdAndClientId(999L, 1L);
        verify(phoneRepository, never()).save(any(Phone.class));
    }
}