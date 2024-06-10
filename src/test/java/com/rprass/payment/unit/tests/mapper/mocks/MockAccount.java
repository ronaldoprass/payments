package com.rprass.payment.unit.tests.mapper.mocks;


import com.rprass.payment.application.dto.AccountDTO;
import com.rprass.payment.domain.model.Account;
import com.rprass.payment.domain.type.Status;
import com.rprass.payment.infrastructure.persistence.entity.AccountEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockAccount {


    public AccountEntity mockEntity() {
        return mockEntity(0);
    }

    public AccountDTO mockDTO() {
        return mockDTO(0);
    }

    public List<AccountEntity> mockEntityList() {
        List<AccountEntity> entities = new ArrayList<AccountEntity>();
        for (int i = 0; i < 14; i++) {
            entities.add(mockEntity(i));
        }
        return entities;
    }

    public List<AccountDTO> mockVOList() {
        List<AccountDTO> dtos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            dtos.add(mockDTO(i));
        }
        return dtos;
    }

    public AccountEntity mockEntity(Integer number) {
        AccountEntity entity = new AccountEntity();
        entity.setId(number.longValue());
        entity.setDueDate(new Date());
        entity.setPaymentDate(new Date());
        entity.setDescription("description" + number);
        entity.setValue(new BigDecimal(10));
        entity.setStatus(Status.PENDENTE);
        return entity;
    }

    public Account mockModel(Integer number) {
        Account model = new Account();
        model.setId(number.longValue());
        model.setDueDate(new Date());
        model.setPaymentDate(new Date());
        model.setDescription("description" + number);
        model.setValue(new BigDecimal(10));
        model.setStatus(Status.PENDENTE);
        return model;
    }

    public AccountDTO mockDTO(Integer number) {
        AccountDTO dto = new AccountDTO();
        dto.setKey(number.longValue());
        dto.setDueDate(new Date());
        dto.setPaymentDate(new Date());
        dto.setDescription("description" + number);
        dto.setValue(new BigDecimal(10));
        dto.setStatus(Status.PENDENTE);
        return dto;
    }

}