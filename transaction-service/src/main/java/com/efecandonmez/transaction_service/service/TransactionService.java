package com.efecandonmez.transaction_service.service;

import com.efecandonmez.transaction_service.client.AccountServiceClient;
import com.efecandonmez.transaction_service.dto.AccountDto;
import com.efecandonmez.transaction_service.model.Transaction;
import com.efecandonmez.transaction_service.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountServiceClient accountServiceClient;

    public TransactionService(TransactionRepository transactionRepository, AccountServiceClient accountServiceClient) {
        this.transactionRepository = transactionRepository;
        this.accountServiceClient = accountServiceClient;
    }

    public Transaction transfer(Long senderId, Long receiverId, BigDecimal amount) {

        AccountDto sender= accountServiceClient.getAccountById(senderId).getBody();
        AccountDto receiver= accountServiceClient.getAccountById(receiverId).getBody();

        if(sender == null || receiver == null) {
            throw new RuntimeException("Sender or receiver account not found");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("yetersiz bakiye");
        }

        Transaction transaction = Transaction.builder()
                .senderAccountId(sender.getId())
                .receiverAccountId(receiver.getId())
                .amount(amount)
                .transactionDate(java.time.LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        accountServiceClient.updateBalance(sender.getId(), amount.negate());
        accountServiceClient.updateBalance(receiver.getId(), amount);

        return transaction;

    }

}
