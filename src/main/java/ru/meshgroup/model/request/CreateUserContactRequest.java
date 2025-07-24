package ru.meshgroup.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@FieldNameConstants
@EqualsAndHashCode(callSuper = true)
public class CreateUserContactRequest extends UserContactRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @NotNull
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal balance;

}
