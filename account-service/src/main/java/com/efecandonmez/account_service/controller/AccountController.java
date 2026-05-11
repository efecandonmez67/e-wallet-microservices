package com.efecandonmez.account_service.controller;

import com.efecandonmez.account_service.dto.CreateAccountRequest;
import com.efecandonmez.account_service.model.Account;
import com.efecandonmez.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest request) {
        Account createdAccount= accountService.createAccount(request.userId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Account> getAccount(@PathVariable Long userId) {
        Account account= accountService.getAccountByUserId(userId);
        return ResponseEntity.ok(account);
    }

}
