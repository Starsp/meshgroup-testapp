package ru.meshgroup.model.request;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class BalanceTransferRequest {

    @NotNull
    private Long recipientId;
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal amount;

}
