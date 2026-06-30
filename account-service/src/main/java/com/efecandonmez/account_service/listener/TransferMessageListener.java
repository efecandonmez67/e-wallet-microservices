package com.efecandonmez.account_service.listener;

import com.efecandonmez.account_service.dto.RefundMessage;
import com.efecandonmez.account_service.dto.TransferMessage;
import com.efecandonmez.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferMessageListener {

    private final AccountService accountService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "transfer_queue")
    public void receiveTransferMessage(TransferMessage message) {
        try {
            accountService.updateBalance(message.getReceiverId(), message.getAmount());
            log.info("transfer işlemi asenkron olarak tamamlandı. alıcı id: {}, miktar: {}", message.getReceiverId(), message.getAmount());

        } catch (Exception e) {
            log.error("transfer mesajı işlenirken hata oluştu: {}", e.getMessage());

            RefundMessage refundMessage = RefundMessage.builder()
                    .senderId(message.getSenderId())
                    .amount(message.getAmount())
                    .reason("alici hesap bulunamadi-receiver not found")
                    .timestamp(LocalDateTime.now())
                    .build();


            rabbitTemplate.convertAndSend("refund_queue", refundMessage);
            log.info("iade mektubu refund_queue'ye basariyla gonderildi. sender id: "+ message.getSenderId());
        }
    }
}