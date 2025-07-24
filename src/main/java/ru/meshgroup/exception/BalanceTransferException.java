package ru.meshgroup.exception;

import lombok.Getter;

public class BalanceTransferException extends RuntimeException {

    @Getter
    private final String code;

    public BalanceTransferException(String code) {
        this.code = code;
    }

}
