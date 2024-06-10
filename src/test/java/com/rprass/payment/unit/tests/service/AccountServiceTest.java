package com.rprass.payment.unit.tests.service;

import com.rprass.payment.application.dto.AccountDTO;
import com.rprass.payment.common.exceptions.RequiredObjectIsNullException;
import com.rprass.payment.domain.model.Account;
import com.rprass.payment.domain.service.AccountService;
import com.rprass.payment.domain.type.Status;
import com.rprass.payment.infrastructure.persistence.repository.JpaAccountRepository;
import com.rprass.payment.unit.tests.mapper.mocks.MockAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    MockAccount input;

    @InjectMocks
    private AccountService service;

    @Mock
    JpaAccountRepository repository;

    @BeforeEach
    void setUpMocks() {
        input = new MockAccount();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        Account model = input.mockModel(1);
        model.setId(1L);
        Date expectedDueDate = model.getDueDate();
        Date expectedPaymentDate = model.getPaymentDate();
        when(repository.findById(1L)).thenReturn(Optional.of(model));

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertEquals("description1", result.getDescription());
        assertEquals(new BigDecimal(10), result.getValue());
        assertEquals(Status.PENDENTE, result.getStatus());
        assertEquals(expectedDueDate.toInstant().truncatedTo(ChronoUnit.SECONDS), result.getDueDate().toInstant().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(expectedPaymentDate.toInstant().truncatedTo(ChronoUnit.SECONDS), result.getPaymentDate().toInstant().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void create() {
        Account model = input.mockModel(1);
        model.setId(1L);
        Date expectedDueDate = model.getDueDate();
        Date expectedPaymentDate = model.getPaymentDate();

        AccountDTO dto = input.mockDTO(1);
        dto.setKey(1L);

        when(repository.save(Mockito.any(Account.class))).thenReturn(model);

        var result = service.create(dto);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertEquals("description1", result.getDescription());
        assertEquals(new BigDecimal(10), result.getValue());
        assertEquals(Status.PENDENTE, result.getStatus());
        assertEquals(expectedDueDate.toInstant().truncatedTo(ChronoUnit.SECONDS), result.getDueDate().toInstant().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(expectedPaymentDate.toInstant().truncatedTo(ChronoUnit.SECONDS), result.getPaymentDate().toInstant().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void createWithNullAccount() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Account model = input.mockModel(1);
        model.setId(1L);
        Date expectedDueDate = model.getDueDate();
        Date expectedPaymentDate = model.getPaymentDate();

        AccountDTO dto = input.mockDTO(1);
        dto.setKey(1L);

        when(repository.update(Mockito.any(Account.class))).thenReturn(model);

        var result = service.update(dto);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertEquals("description1", result.getDescription());
        assertEquals(new BigDecimal(10), result.getValue());
        assertEquals(Status.PENDENTE, result.getStatus());
        assertEquals(expectedDueDate.toInstant().truncatedTo(ChronoUnit.SECONDS), result.getDueDate().toInstant().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(expectedPaymentDate.toInstant().truncatedTo(ChronoUnit.SECONDS), result.getPaymentDate().toInstant().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void updateWithNullAccount() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        Account model = input.mockModel(1);
        model.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(model));
        service.delete(1L);
    }


}

