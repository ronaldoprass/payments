package com.rprass.payment.infrastructure.converter;


import com.rprass.payment.application.dto.AccountDTO;
import com.rprass.payment.domain.model.Account;
import com.rprass.payment.infrastructure.persistence.entity.AccountEntity;

public class AccountConverter {

    public static AccountEntity toEntity(Account account) {
        return AccountEntity.builder()
                .id(account.getId())
                .dueDate(account.getDueDate())
                .paymentDate(account.getPaymentDate())
                .value(account.getValue())
                .description(account.getDescription())
                .status(account.getStatus())
                .build();
    }

    public static Account toDomainModel(AccountEntity accountEntity) {
        return Account.builder()
                .id(accountEntity.getId())
                .dueDate(accountEntity.getDueDate())
                .paymentDate(accountEntity.getPaymentDate())
                .value(accountEntity.getValue())
                .description(accountEntity.getDescription())
                .status(accountEntity.getStatus())
                .build();
    }

    public static AccountDTO toDTO(AccountEntity accountEntity) {
        return AccountDTO.builder()
                .key(accountEntity.getId())
                .dueDate(accountEntity.getDueDate())
                .paymentDate(accountEntity.getPaymentDate())
                .value(accountEntity.getValue())
                .description(accountEntity.getDescription())
                .status(accountEntity.getStatus())
                .build();
    }

    public static AccountEntity toEntity(AccountDTO accountDTO) {
        return AccountEntity.builder()
                .id(accountDTO.getKey())
                .dueDate(accountDTO.getDueDate())
                .paymentDate(accountDTO.getPaymentDate())
                .value(accountDTO.getValue())
                .description(accountDTO.getDescription())
                .status(accountDTO.getStatus())
                .build();
    }

    public static Account toDomainModel(AccountDTO accountDTO) {
        return Account.builder()
                .id(accountDTO.getKey())
                .dueDate(accountDTO.getDueDate())
                .paymentDate(accountDTO.getPaymentDate())
                .value(accountDTO.getValue())
                .description(accountDTO.getDescription())
                .status(accountDTO.getStatus())
                .build();
    }

    public static AccountDTO toDTO(Account account) {
        return AccountDTO.builder()
                .key(account.getId())
                .dueDate(account.getDueDate())
                .paymentDate(account.getPaymentDate())
                .value(account.getValue())
                .description(account.getDescription())
                .status(account.getStatus())
                .build();
    }
}
