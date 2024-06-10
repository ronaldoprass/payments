package com.rprass.payment.infrastructure.persistence.entity;

import com.rprass.payment.domain.type.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conta")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "data_vencimento", nullable = false)
    private Date dueDate;

    @Column(name = "data_pagamento")
    private Date paymentDate;

    @NotNull
    @Column(name = "valor", nullable = false)
    private BigDecimal value;

    @NotNull
    @Column(name = "descricao", nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private Status status;

}
