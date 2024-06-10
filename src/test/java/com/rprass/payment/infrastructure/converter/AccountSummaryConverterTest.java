package com.rprass.payment.infrastructure.converter;

import com.rprass.payment.application.dto.AccountSummaryDTO;
import com.rprass.payment.domain.model.AccountSummary;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountSummaryConverterTest {

    @Test
    void testToDomainModel() {
        AccountSummaryDTO accountSummaryDTO = AccountSummaryDTO.builder()
                .period("2023-01")
                .totalValue(new BigDecimal("1000.00"))
                .build();

        AccountSummary accountSummary = AccountSummaryConverter.toDomainModel(accountSummaryDTO);

        assertNotNull(accountSummary);
        assertEquals(accountSummaryDTO.getPeriod(), accountSummary.getPeriod());
        assertEquals(accountSummaryDTO.getTotalValue(), accountSummary.getTotalValue());
    }

    @Test
    void testToDTO() {
        AccountSummary accountSummary = AccountSummary.builder()
                .period("2023-01")
                .totalValue(new BigDecimal("1000.00"))
                .build();

        AccountSummaryDTO accountSummaryDTO = AccountSummaryConverter.toDTO(accountSummary);

        assertNotNull(accountSummaryDTO);
        assertEquals(accountSummary.getPeriod(), accountSummaryDTO.getPeriod());
        assertEquals(accountSummary.getTotalValue(), accountSummaryDTO.getTotalValue());
    }
}
