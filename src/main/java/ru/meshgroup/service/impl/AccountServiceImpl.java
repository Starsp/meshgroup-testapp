package ru.meshgroup.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.meshgroup.exception.BalanceTransferException;
import ru.meshgroup.model.request.BalanceTransferRequest;
import ru.meshgroup.persistance.model.Account;
import ru.meshgroup.persistance.repository.AccountRepository;
import ru.meshgroup.properties.AccountProperties;
import ru.meshgroup.security.JwtTokenUtil;
import ru.meshgroup.service.AccountService;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountProperties accountProperties;
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    @Transactional
    public void increaseBalance(Long accountId) {
        log.debug("Increase balance for account {}", accountId);
        try {
            Account account = accountRepository.getAccountByIdWithLock(accountId);
            BigDecimal newBalance = account.getBalance()
                    .multiply(accountProperties.getIncreasePercentage())
                    .setScale(4, RoundingMode.HALF_UP);
            BigDecimal maxAllowedBalance = account.getInitialDeposit()
                    .multiply(accountProperties.getMaxAllowedMultiplier())
                    .setScale(4, RoundingMode.HALF_UP);

            account.setBalance(min(newBalance, maxAllowedBalance));
        } catch (Exception e) {
            log.debug(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void transferToUser(BalanceTransferRequest request) {
        Long currentUserID = jwtTokenUtil.extractUserIdJwt();

        log.error("Start transfer from user {} to user {}", currentUserID, request.getRecipientId());

        if (request.getRecipientId().equals(currentUserID)) {
            throw new BalanceTransferException("SELF_TRANSFER");
        }

        Long firstId = Math.min(currentUserID, request.getRecipientId());
        Long secondId = Math.max(currentUserID, request.getRecipientId());

        Account firstAccount = accountRepository.findByUserIdWithLock(firstId);
        Account secondAccount = accountRepository.findByUserIdWithLock(secondId);

        Account fromAccount = firstAccount.getUserId().equals(currentUserID) ? firstAccount : secondAccount;
        Account toAccount = secondAccount.getUserId().equals(request.getRecipientId()) ? firstAccount : secondAccount;

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BalanceTransferException("INSUFFICIENT_FUNDS");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
    }

    private BigDecimal min(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) < 0 ? a : b;
    }
}
