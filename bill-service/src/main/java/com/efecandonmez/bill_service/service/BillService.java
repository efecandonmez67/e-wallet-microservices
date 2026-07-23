package com.efecandonmez.bill_service.service;

import com.efecandonmez.bill_service.config.RabbitMQConfig;
import com.efecandonmez.bill_service.dto.BillPaymentMessage;
import com.efecandonmez.bill_service.model.Bill;
import com.efecandonmez.bill_service.repository.BillRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final RabbitTemplate rabbitTemplate;

    public BillService(BillRepository billRepository, RabbitTemplate rabbitTemplate) {
        this.billRepository = billRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Bill> getUnpaidBills(Long accountId) {
        return billRepository.findByAccountIdAndIsPaidFalse(accountId);
    }

    public String payBill(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Fatura bulunamadı!"));

        if (bill.getIsPaid()) {
            return "Bu fatura zaten ödenmiş!";
        }

        BillPaymentMessage message = new BillPaymentMessage(bill.getId(), bill.getAccountId(), bill.getAmount());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.BILL_PAYMENT_ROUTING_KEY, message);

        bill.setIsPaid(true);
        billRepository.save(bill);

        return "Fatura ödeme işlemi başarılı ve hesap bakiyesinden düşülmesi için kuyruğa iletildi!";
    }
}