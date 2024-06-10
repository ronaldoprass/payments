package com.rprass.payment.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountSummary {
    private String period;
    private BigDecimal totalValue;
}
