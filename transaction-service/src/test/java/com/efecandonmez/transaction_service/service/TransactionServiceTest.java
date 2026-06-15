package com.efecandonmez.transaction_service.service;

import com.efecandonmez.transaction_service.client.AccountServiceClient;
import com.efecandonmez.transaction_service.dto.AccountDto;
import com.efecandonmez.transaction_service.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountServiceClient accountServiceClient;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void transfer_withInsufficientBalance_shouldThrowRuntimeException() {
        Long senderId = 1L;
        Long receiverId = 2L;
        BigDecimal transferAmount = new BigDecimal("150.00");

        AccountDto senderAccount = AccountDto.builder()
                .id(10L)
                .customerId(senderId)
                .balance(new BigDecimal("100.00"))
                .build();

        AccountDto receiverAccount = AccountDto.builder()
                .id(20L)
                .customerId(receiverId)
                .balance(new BigDecimal("500.00"))
                .build();

        Mockito.when(accountServiceClient.getAccountById(senderId))
                .thenReturn(org.springframework.http.ResponseEntity.ok(senderAccount));

        Mockito.when(accountServiceClient.getAccountById(receiverId))
                .thenReturn(org.springframework.http.ResponseEntity.ok(receiverAccount));


        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> transactionService.transfer(senderId, receiverId, transferAmount)
        );

        org.junit.jupiter.api.Assertions.assertEquals("yetersiz bakiye", exception.getMessage());
    }

    @Test
    void transfer_withSufficientBalance_shouldCompleteSuccessfully() {

        Long senderId = 1L;
        Long receiverId = 2L;
        BigDecimal transferAmount = new BigDecimal("50.00");

        AccountDto senderAccount= AccountDto.builder()
                .id(10L)
                .customerId(senderId)
                .balance(new BigDecimal("1000.00"))
                .build();

        AccountDto receiverAccount= AccountDto.builder()
                .id(20L)
                .customerId(receiverId)
                .balance(new BigDecimal("500.00"))
                .build();


        Mockito.when(accountServiceClient.getAccountById(senderId))
                .thenReturn(org.springframework.http.ResponseEntity.ok(senderAccount));

        Mockito.when(accountServiceClient.getAccountById(receiverId))
                .thenReturn(org.springframework.http.ResponseEntity.ok(receiverAccount));

        transactionService.transfer(senderId, receiverId, transferAmount);

        Mockito.verify(accountServiceClient).updateBalance(senderAccount.getId(), transferAmount.negate());

        Mockito.verify(accountServiceClient).updateBalance(receiverAccount.getId(), transferAmount);

    }

}
