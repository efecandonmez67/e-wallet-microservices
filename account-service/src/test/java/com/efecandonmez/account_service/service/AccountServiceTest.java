package com.efecandonmez.account_service.service;

import com.efecandonmez.account_service.model.Account;
import com.efecandonmez.account_service.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;


    @Test
    public void testWithDraw_Successfull() {
        Long accountId = 1L;
        Double withdrawAmount = 50.0;

        Account dummyAccount = new Account();
        dummyAccount.setId(accountId);
        dummyAccount.setBalance(BigDecimal.valueOf(100.0));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(dummyAccount));

        when(accountRepository.save(dummyAccount)).thenReturn(dummyAccount);

        Account result= accountService.withDraw(accountId, BigDecimal.valueOf(withdrawAmount));

        assertEquals(BigDecimal.valueOf(50.0), result.getBalance());

        verify(accountRepository, times(1)).save(dummyAccount);

    }

    @Test
    public void testWithDraw_Failure() {
        Long accountId = 1L;
        Double withdrawAmount = 150.0;

        Account dummyAccount = new Account();
        dummyAccount.setId(accountId);
        dummyAccount.setBalance(BigDecimal.valueOf(100.0));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(dummyAccount));

        assertThrows(RuntimeException.class, () -> {
            accountService.withDraw(accountId, BigDecimal.valueOf(withdrawAmount));
        });

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testDeposit_Successfull() {
        Long accountId = 1L;
        Double depositAmount = 50.0;

        Account dummyAccount = new Account();
        dummyAccount.setId(accountId);
        dummyAccount.setBalance(BigDecimal.valueOf(100.0));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(dummyAccount));

        when(accountRepository.save(any(Account.class))).thenReturn(dummyAccount);

        Account result= accountService.deposit(accountId, BigDecimal.valueOf(depositAmount));

        assertEquals(BigDecimal.valueOf(150.0), result.getBalance());

        verify(accountRepository, times(1)).save(dummyAccount);
    }

    @Test
    public void testAccountNotFound() {
        Long nonExistentAccountId = 99L;

        when(accountRepository.findById(nonExistentAccountId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            accountService.deposit(nonExistentAccountId, BigDecimal.valueOf(50.0));
        });

        verify(accountRepository, never()).save(any(Account.class));

    }



}