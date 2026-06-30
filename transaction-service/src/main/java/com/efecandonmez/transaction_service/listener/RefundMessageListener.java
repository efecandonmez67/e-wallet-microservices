package com.efecandonmez.transaction_service.listener;

import com.efecandonmez.transaction_service.client.AccountServiceClient;
import com.efecandonmez.transaction_service.dto.RefundMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefundMessageListener {

    private final AccountServiceClient accountServiceClient;

    @RabbitListener(queues = "refund_queue")
    public void receiveRefundMessage(RefundMessage message) {

        log.warn("iade mektubu alindi. sebep: {}. iade edilecek hesap: {}, miktar: {}",
                message.getReason(), message.getSenderId(), message.getAmount());

        try {
            accountServiceClient.updateBalance(message.getSenderId(), message.getAmount());
            log.info("iade basariya tamamlandi. para {} numaralı hesaba geri yuklendi.", message.getSenderId());
        } catch (Exception e) {
            log.error("iade islemi sirasinda bir hata olustu. {}", e.getMessage());
        }

    }

}
