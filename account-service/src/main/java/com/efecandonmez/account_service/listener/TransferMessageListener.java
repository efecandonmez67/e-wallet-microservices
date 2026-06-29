package com.efecandonmez.account_service.listener;

import com.efecandonmez.account_service.dto.TransferMessage;
import com.efecandonmez.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferMessageListener {

    private final AccountService accountService;

    @RabbitListener(queues = "transfer_queue")
    public void receiveTransferMessage(TransferMessage message) {
        try {
            accountService.updateBalance(message.getReceiverId(), message.getAmount());

            log.info("transfer işlemi asenkron olarak tamamlandı. alıcı id: {}, miktar: {}", message.getReceiverId(), message.getAmount());

        } catch (Exception e) {
            log.error("transfer mesajı işlenirken hata oluştu: {}", e.getMessage());
        }
    }
}