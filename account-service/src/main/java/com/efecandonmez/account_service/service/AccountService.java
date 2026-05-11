package com.efecandonmez.account_service.service;

import com.efecandonmez.account_service.exception.AccountNotFoundException;
import com.efecandonmez.account_service.model.Account;
import com.efecandonmez.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(Long userId) {
        Account newAccount = Account.builder()
                        .userId(userId)
                                .build();

        return accountRepository.save(newAccount);
    }

    public Account getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new AccountNotFoundException("No account found for this user. UserId: " + userId));
    }

}
