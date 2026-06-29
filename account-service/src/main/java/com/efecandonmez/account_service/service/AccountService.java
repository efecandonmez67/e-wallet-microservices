package com.efecandonmez.account_service.service;

import com.efecandonmez.account_service.exception.AccountNotFoundException;
import com.efecandonmez.account_service.model.Account;
import com.efecandonmez.account_service.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

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

    public void updateBalance(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new AccountNotFoundException("No account found for this user. accountId: " + accountId)
        );

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

    }

}
