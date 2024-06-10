package com.rprass.payment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rprass.payment.domain.type.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonPropertyOrder({"id", "dueDate", "paymentDate", "value", "description", "status"})
public class AccountDTO extends RepresentationModel<AccountDTO> implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long key;

    @NotNull(message = "The due date cannot be null")
    private Date dueDate;

    private Date paymentDate;

    @NotNull(message = "he value cannot be null")
    private BigDecimal value;

    @NotBlank(message = "he description cannot be blank")
    private String description;

    @NotNull(message = "The status cannot be null")
    private Status status;
}
