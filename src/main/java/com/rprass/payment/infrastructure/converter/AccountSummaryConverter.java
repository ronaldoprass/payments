package com.rprass.payment.infrastructure.converter;


import com.rprass.payment.application.dto.AccountSummaryDTO;
import com.rprass.payment.domain.model.AccountSummary;

public class AccountSummaryConverter {


    public static AccountSummary toDomainModel(AccountSummaryDTO accountSummaryDTO) {
        return AccountSummary.builder()
                .period(accountSummaryDTO.getPeriod())
                .totalValue(accountSummaryDTO.getTotalValue())
                .build();
    }

    public static AccountSummaryDTO toDTO(AccountSummary accountSummary) {
        return AccountSummaryDTO.builder()
                .period(accountSummary.getPeriod())
                .totalValue(accountSummary.getTotalValue())
                .build();
    }
}
