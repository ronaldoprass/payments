package com.rprass.payment.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "The due date cannot be null")
    private Date dueDate;

    @NotBlank(message = "he description cannot be blank")
    private String description;
}
