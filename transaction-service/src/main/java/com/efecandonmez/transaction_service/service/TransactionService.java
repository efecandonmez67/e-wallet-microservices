package com.efecandonmez.transaction_service.service;

import com.efecandonmez.transaction_service.client.AccountServiceClient;
import com.efecandonmez.transaction_service.config.RabbitMQConfig;
import com.efecandonmez.transaction_service.dto.TransferMessage;
import com.efecandonmez.transaction_service.model.Transaction;
import com.efecandonmez.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountServiceClient accountServiceClient;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public void transfer(Long senderId, Long receiverId, BigDecimal amount) {

        accountServiceClient.withDraw(senderId, amount);

        Transaction transaction = Transaction.builder()
                .senderAccountId(senderId)
                .receiverAccountId(receiverId)
                .amount(amount)
                .transactionDate(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);

        TransferMessage message= TransferMessage.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .amount(amount)
                .build();


        rabbitTemplate.convertAndSend(RabbitMQConfig.TRANSFER_QUEUE, message);

        System.out.println("mektup rabbite başarıyla bırakıldı." + message);

    }

    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository.findBySenderAccountIdOrReceiverAccountIdOrderByTransactionDateDesc(accountId, accountId);
    }

}
