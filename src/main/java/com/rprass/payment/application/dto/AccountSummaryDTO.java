package com.rprass.payment.application.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountSummaryDTO extends RepresentationModel<AccountSummaryDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String period;
    private BigDecimal totalValue;
}
