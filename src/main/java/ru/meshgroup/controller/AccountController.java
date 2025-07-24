package ru.meshgroup.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.meshgroup.model.request.BalanceTransferRequest;
import ru.meshgroup.service.AccountService;

import javax.validation.Valid;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/transfer")
    public void transfer(@Valid @RequestBody BalanceTransferRequest request) {
        accountService.transferToUser(request);
    }

}
