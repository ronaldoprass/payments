package com.rprass.payment.application.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AccountPayableDTO extends RepresentationModel<AccountPayableDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date dueDate;
    private String description;
}
