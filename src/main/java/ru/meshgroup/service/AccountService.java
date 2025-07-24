package ru.meshgroup.service;

import ru.meshgroup.model.request.BalanceTransferRequest;

public interface AccountService {

    void increaseBalance(Long accountId);

    void transferToUser(BalanceTransferRequest request);

}
