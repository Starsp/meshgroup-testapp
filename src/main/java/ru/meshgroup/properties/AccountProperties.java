package ru.meshgroup.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Data
@ConfigurationProperties(prefix = "account")
public class AccountProperties {

    private BigDecimal increasePercentage;
    private BigDecimal maxAllowedMultiplier;
    private Integer increaseIntervalSeconds;

}
