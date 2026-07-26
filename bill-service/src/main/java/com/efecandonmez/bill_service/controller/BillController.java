package com.efecandonmez.bill_service.controller;

import com.efecandonmez.bill_service.model.Bill;
import com.efecandonmez.bill_service.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Bill>> getUnpaidBills(@PathVariable Long accountId) {
        return ResponseEntity.ok(billService.getUnpaidBills(accountId));
    }

    @PostMapping("/pay/{billId}")
    public ResponseEntity<String> payBill(@PathVariable Long billId) {
        try {
            String result = billService.payBill(billId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("İşlem sırasında bir hata oluştu.");
        }
    }
}