package com.efecandonmez.account_service.service;

import com.efecandonmez.account_service.exception.AccountNotFoundException;
import com.efecandonmez.account_service.model.Account;
import com.efecandonmez.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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

    @Transactional
    public Account deposit(Long accountId, BigDecimal amount) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("No account found for this user. accountId: " + accountId));
        account.setBalance(account.getBalance().add(amount));

        return accountRepository.save(account);
    }

    @Transactional
    public Account withdraw(Long accountId, BigDecimal amount) {

        Account account= accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Insufficient balance: " + accountId) );

        if(account.getBalance().compareTo(amount) >= 0) {
            account.setBalance(account.getBalance().subtract(amount));
        } else {
            throw new IllegalArgumentException("Insufficient balance, your balance is: " + account.getBalance());
        }

        return accountRepository.save(account);
    }

}
