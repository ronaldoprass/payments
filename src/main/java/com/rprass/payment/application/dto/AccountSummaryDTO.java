package com.rprass.payment.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "he period cannot be blank")
    private String period;

    @NotNull(message = "he totalValue cannot be null")
    private BigDecimal totalValue;
}
