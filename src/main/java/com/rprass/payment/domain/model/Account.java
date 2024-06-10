package com.rprass.payment.domain.model;

import com.rprass.payment.domain.type.Status;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private Long id;

    private Date dueDate;

    private Date paymentDate;

    private BigDecimal value;

    private String description;

    private Status status;
}
