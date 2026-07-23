package com.efecandonmez.account_service.listener;

import com.efecandonmez.account_service.dto.BillPaymentMessage;
import com.efecandonmez.account_service.model.Account;
import com.efecandonmez.account_service.repository.AccountRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BillPaymentListener {

    private final AccountRepository accountRepository;
    private final RabbitTemplate rabbitTemplate;

    public BillPaymentListener(AccountRepository accountRepository, RabbitTemplate rabbitTemplate) {
        this.accountRepository = accountRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "bill.payment.queue")
    @Transactional
    public void handleBillPayment(BillPaymentMessage message) {
        System.out.println("TETİKLENDİ: Fatura ödeme mesajı alındı! Hesap ID: " + message.accountId() + " Tutar: " + message.amount());

        Account account = accountRepository.findById(message.accountId())
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadı!"));

        if (account.getBalance().compareTo(message.amount()) < 0) {
            System.err.println("YETERSİZ BAKİYE: Fatura tahsil edilemedi. İptal işlemi (Rollback) başlatılıyor...");
            rabbitTemplate.convertAndSend("bill.rollback.queue", message);
            return;
        }

        account.setBalance(account.getBalance().subtract(message.amount()));
        accountRepository.save(account);

        System.out.println("BAŞARILI: Fatura tutarı (" + message.amount() + "₺) bakiyeden düşüldü. Yeni bakiye: " + account.getBalance());
    }
}