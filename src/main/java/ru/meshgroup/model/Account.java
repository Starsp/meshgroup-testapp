package ru.meshgroup.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {

    private Long id;
    private BigDecimal balance;

}
