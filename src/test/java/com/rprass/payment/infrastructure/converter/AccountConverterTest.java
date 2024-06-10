package com.rprass.payment.infrastructure.converter;

import com.rprass.payment.application.dto.AccountDTO;
import com.rprass.payment.domain.model.Account;
import com.rprass.payment.infrastructure.persistence.entity.AccountEntity;
import com.rprass.payment.domain.type.Status;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountConverterTest {

    @Test
    void testToEntityFromDomain() {
        Account account = Account.builder()
                .id(1L)
                .dueDate(new Date())
                .paymentDate(new Date())
                .value(new BigDecimal("100.00"))
                .description("Test description")
                .status(Status.PENDENTE)
                .build();

        AccountEntity accountEntity = AccountConverter.toEntity(account);

        assertNotNull(accountEntity);
        assertEquals(account.getId(), accountEntity.getId());
        assertEquals(account.getDueDate(), accountEntity.getDueDate());
        assertEquals(account.getPaymentDate(), accountEntity.getPaymentDate());
        assertEquals(account.getValue(), accountEntity.getValue());
        assertEquals(account.getDescription(), accountEntity.getDescription());
        assertEquals(account.getStatus(), accountEntity.getStatus());
    }

    @Test
    void testToDomainModelFromEntity() {
        AccountEntity accountEntity = AccountEntity.builder()
                .id(1L)
                .dueDate(new Date())
                .paymentDate(new Date())
                .value(new BigDecimal("100.00"))
                .description("Test description")
                .status(Status.PENDENTE)
                .build();

        Account account = AccountConverter.toDomainModel(accountEntity);

        assertNotNull(account);
        assertEquals(accountEntity.getId(), account.getId());
        assertEquals(accountEntity.getDueDate(), account.getDueDate());
        assertEquals(accountEntity.getPaymentDate(), account.getPaymentDate());
        assertEquals(accountEntity.getValue(), account.getValue());
        assertEquals(accountEntity.getDescription(), account.getDescription());
        assertEquals(accountEntity.getStatus(), account.getStatus());
    }

    @Test
    void testToDTOFromEntity() {
        AccountEntity accountEntity = AccountEntity.builder()
                .id(1L)
                .dueDate(new Date())
                .paymentDate(new Date())
                .value(new BigDecimal("100.00"))
                .description("Test description")
                .status(Status.PENDENTE)
                .build();

        AccountDTO accountDTO = AccountConverter.toDTO(accountEntity);

        assertNotNull(accountDTO);
        assertEquals(accountEntity.getId(), accountDTO.getKey());
        assertEquals(accountEntity.getDueDate(), accountDTO.getDueDate());
        assertEquals(accountEntity.getPaymentDate(), accountDTO.getPaymentDate());
        assertEquals(accountEntity.getValue(), accountDTO.getValue());
        assertEquals(accountEntity.getDescription(), accountDTO.getDescription());
        assertEquals(accountEntity.getStatus(), accountDTO.getStatus());
    }

    @Test
    void testToEntityFromDTO() {
        AccountDTO accountDTO = AccountDTO.builder()
                .key(1L)
                .dueDate(new Date())
                .paymentDate(new Date())
                .value(new BigDecimal("100.00"))
                .description("Test description")
                .status(Status.PENDENTE)
                .build();

        AccountEntity accountEntity = AccountConverter.toEntity(accountDTO);

        assertNotNull(accountEntity);
        assertEquals(accountDTO.getKey(), accountEntity.getId());
        assertEquals(accountDTO.getDueDate(), accountEntity.getDueDate());
        assertEquals(accountDTO.getPaymentDate(), accountEntity.getPaymentDate());
        assertEquals(accountDTO.getValue(), accountEntity.getValue());
        assertEquals(accountDTO.getDescription(), accountEntity.getDescription());
        assertEquals(accountDTO.getStatus(), accountEntity.getStatus());
    }

    @Test
    void testToDomainModelFromDTO() {
        AccountDTO accountDTO = AccountDTO.builder()
                .key(1L)
                .dueDate(new Date())
                .paymentDate(new Date())
                .value(new BigDecimal("100.00"))
                .description("Test description")
                .status(Status.PENDENTE)
                .build();

        Account account = AccountConverter.toDomainModel(accountDTO);

        assertNotNull(account);
        assertEquals(accountDTO.getKey(), account.getId());
        assertEquals(accountDTO.getDueDate(), account.getDueDate());
        assertEquals(accountDTO.getPaymentDate(), account.getPaymentDate());
        assertEquals(accountDTO.getValue(), account.getValue());
        assertEquals(accountDTO.getDescription(), account.getDescription());
        assertEquals(accountDTO.getStatus(), account.getStatus());
    }

    @Test
    void testToDTOFromDomain() {
        Account account = Account.builder()
                .id(1L)
                .dueDate(new Date())
                .paymentDate(new Date())
                .value(new BigDecimal("100.00"))
                .description("Test description")
                .status(Status.PENDENTE)
                .build();

        AccountDTO accountDTO = AccountConverter.toDTO(account);

        assertNotNull(accountDTO);
        assertEquals(account.getId(), accountDTO.getKey());
        assertEquals(account.getDueDate(), accountDTO.getDueDate());
        assertEquals(account.getPaymentDate(), accountDTO.getPaymentDate());
        assertEquals(account.getValue(), accountDTO.getValue());
        assertEquals(account.getDescription(), accountDTO.getDescription());
        assertEquals(account.getStatus(), accountDTO.getStatus());
    }
}
