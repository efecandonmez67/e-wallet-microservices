package com.efecandonmez.account_service.listener; // Kendi paket ismine göre uyarla

import com.efecandonmez.account_service.model.Account;
import com.efecandonmez.account_service.repository.AccountRepository;
import com.efecandonmez.account_service.dto.TransferMessage;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationListener {

    private final AccountRepository accountRepository;

    public NotificationListener(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @RabbitListener(queues = "transfer_queue")
    public void handleTransferNotification(TransferMessage transferMessage) {
        System.out.println("RabbitMQ'dan transfer mesajı alındı! Alıcı ID: " + transferMessage.getReceiverId());

        Optional<Account> receiverAccount = accountRepository.findById(transferMessage.getReceiverId());

        receiverAccount.ifPresent(account -> {
            String fcmToken = account.getFcmToken();

            if (fcmToken != null && !fcmToken.isEmpty()) {

                Notification notification = Notification.builder()
                        .setTitle("Gelen Transfer 💸")
                        .setBody("Hesabınıza " + transferMessage.getAmount() + " ₺ para transferi gerçekleşti.")
                        .build();

                Message firebaseMessage = Message.builder()
                        .setToken(fcmToken)
                        .setNotification(notification)
                        .setAndroidConfig(AndroidConfig.builder()
                                .setPriority(AndroidConfig.Priority.HIGH)
                                .setNotification(AndroidNotification.builder()
                                        .setSound("default")
                                        .build())
                                .build())
                        .build();

                try {
                    String response = FirebaseMessaging.getInstance().send(firebaseMessage);
                    System.out.println("Firebase Bildirimi başarıyla gönderildi: " + response);
                } catch (Exception e) {
                    System.err.println("Firebase Bildirimi gönderilirken hata oluştu: " + e.getMessage());
                }
            } else {
                System.out.println("Alıcının FCM Token'ı yok, bildirim atılamadı.");
            }
        });
    }
}