package com.efecandonmez.bill_service.listener;

import com.efecandonmez.bill_service.dto.BillPaymentMessage;
import com.efecandonmez.bill_service.model.Bill;
import com.efecandonmez.bill_service.repository.BillRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BillRollbackListener {

    private final BillRepository billRepository;

    public BillRollbackListener(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @RabbitListener(queues = "bill.rollback.queue")
    public void handleBillRollback(BillPaymentMessage message) {
        System.err.println("İPTAL GELDİ: Bakiye yetersiz! Fatura ID: " + message.billId() + " geri alınıyor...");

        Bill bill = billRepository.findById(message.billId()).orElse(null);
        if (bill != null) {
            bill.setIsPaid(false);
            billRepository.save(bill);
            System.out.println("BAŞARILI: Fatura başarıyla eski haline getirildi.");
        }
    }
}