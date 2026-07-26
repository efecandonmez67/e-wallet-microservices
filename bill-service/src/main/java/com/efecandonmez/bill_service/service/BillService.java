package com.efecandonmez.bill_service.service;

import com.efecandonmez.bill_service.config.RabbitMQConfig;
import com.efecandonmez.bill_service.dto.BillPaymentMessage;
import com.efecandonmez.bill_service.model.Bill;
import com.efecandonmez.bill_service.repository.BillRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;

    // RestTemplate Inject edildi
    public BillService(BillRepository billRepository, RabbitTemplate rabbitTemplate, RestTemplate restTemplate) {
        this.billRepository = billRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
    }

    public List<Bill> getUnpaidBills(Long accountId) {
        return billRepository.findByAccountIdAndIsPaidFalse(accountId);
    }

    public String payBill(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Fatura bulunamadı!"));

        if (bill.getIsPaid()) {
            throw new IllegalArgumentException("Bu fatura zaten ödenmiş!");
        }

        // --- SENKRON BAKİYE KONTROLÜ ---
        // TODO: Buradaki URL, senin hesap servisinin Eureka'daki adına ve endpoint'ine göre güncellenecek
        String accountServiceUrl = "http://ACCOUNT-SERVICE/api/v1/accounts/" + bill.getAccountId() + "/balance";

        try {
            // Bakiyeyi doğrudan BigDecimal olarak çekiyoruz
            java.math.BigDecimal currentBalance = restTemplate.getForObject(accountServiceUrl, java.math.BigDecimal.class);

            // Bakiye null ise veya faturadan (bill.getAmount()) küçükse işlemi kes!
            if (currentBalance == null || currentBalance.compareTo(bill.getAmount()) < 0) {
                throw new IllegalArgumentException("Bakiyeniz bu faturayı ödemek için yetersiz.");
            }
        } catch (IllegalArgumentException e) {
            throw e; // Yetersiz bakiye hatasını Controller'a pasla
        } catch (Exception e) {
            throw new RuntimeException("Bakiye kontrolü yapılamadı, hesap servisine ulaşılamıyor.");
        }
        // -------------------------------

        // Bakiye yetiyorsa normal akışa (RabbitMQ) devam et
        BillPaymentMessage message = new BillPaymentMessage(bill.getId(), bill.getAccountId(), bill.getAmount());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.BILL_PAYMENT_ROUTING_KEY, message);

        bill.setIsPaid(true);
        billRepository.save(bill);

        return "Fatura başarıyla ödendi!";
    }
}