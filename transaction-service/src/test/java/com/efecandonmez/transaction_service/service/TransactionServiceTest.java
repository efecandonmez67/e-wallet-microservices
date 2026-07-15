package com.efecandonmez.transaction_service.service;

import com.efecandonmez.transaction_service.client.AccountServiceClient;
import com.efecandonmez.transaction_service.config.RabbitMQConfig;
import com.efecandonmez.transaction_service.dto.TransferMessage;
import com.efecandonmez.transaction_service.exception.InsufficientBalanceException;
import com.efecandonmez.transaction_service.model.Transaction;
import com.efecandonmez.transaction_service.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountServiceClient accountServiceClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    public void transfer_Successfull() {

        Long senderId = 1L;
        Long receiverId = 2L;
        BigDecimal transferAmount= new BigDecimal("50.00");

        transactionService.transfer(senderId, receiverId, transferAmount);

        verify(accountServiceClient, times(1)).withDraw(senderId, transferAmount);

        verify(transactionRepository, times(1)).save(any(Transaction.class));

        verify(rabbitTemplate, times(1)).convertAndSend(eq(RabbitMQConfig.TRANSFER_QUEUE), any(TransferMessage.class));
    }

    @Test
    public void transfer_withInsufficientBalance_shouldThrowException() {
        Long senderId = 1L;
        Long receiverId = 2L;
        BigDecimal transferAmount= new BigDecimal("150.00");

        doThrow(new InsufficientBalanceException("yetersiz bakiye")).when(accountServiceClient).withDraw(senderId, transferAmount);

        assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.transfer(senderId, receiverId, transferAmount);
        });

        verify(transactionRepository, never()).save(any(Transaction.class));

        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(TransferMessage.class));
    }



}
