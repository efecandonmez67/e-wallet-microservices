package com.efecandonmez.transaction_service.service;

import com.efecandonmez.transaction_service.client.AccountServiceClient;
import com.efecandonmez.transaction_service.dto.AccountDto;
import com.efecandonmez.transaction_service.exception.InsufficientBalanceException;
import com.efecandonmez.transaction_service.model.Transaction;
import com.efecandonmez.transaction_service.repository.TransactionRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountServiceClient accountServiceClient;

    public TransactionService(TransactionRepository transactionRepository, AccountServiceClient accountServiceClient) {
        this.transactionRepository = transactionRepository;
        this.accountServiceClient = accountServiceClient;
    }

    @Transactional
    public Transaction transfer(Long senderId, Long receiverId, BigDecimal amount) {

        AccountDto sender;
        AccountDto receiver;

        try {
            sender = accountServiceClient.getAccountById(senderId).getBody();
            receiver = accountServiceClient.getAccountById(receiverId).getBody();
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Account not found");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("yetersiz bakiye");
        }

        Transaction transaction = Transaction.builder()
                .senderAccountId(sender.getId())
                .receiverAccountId(receiver.getId())
                .amount(amount)
                .transactionDate(java.time.LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        accountServiceClient.updateBalance(sender.getId(), amount.negate());

        try {
            accountServiceClient.updateBalance(receiver.getId(), amount.negate());
        } catch (Exception e) {
            accountServiceClient.updateBalance(sender.getId(), amount);

            throw new RuntimeException("Transfer sırasında karşı servise ulaşılamadı. İşlem iptal edildi ve bakiye iade edildi.");
        }

        return transaction;

    }

}
