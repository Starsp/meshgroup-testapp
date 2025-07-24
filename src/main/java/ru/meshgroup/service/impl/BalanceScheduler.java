package ru.meshgroup.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.meshgroup.persistance.repository.AccountRepository;
import ru.meshgroup.properties.AccountProperties;
import ru.meshgroup.service.AccountService;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class BalanceScheduler {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AccountProperties accountProperties;

    @Scheduled(fixedRateString = "${account.increase-interval-seconds}", timeUnit = TimeUnit.SECONDS)
    public void increaseBalance() {
        log.error("Increase balance started");
        accountRepository.getAccountIdForIncrease(accountProperties.getMaxAllowedMultiplier())
                .forEach(accountService::increaseBalance);
    }

}
