package com.efecandonmez.transaction_service.controller;

import com.efecandonmez.transaction_service.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController{

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam BigDecimal amount) {

        transactionService.transfer(senderId, receiverId, amount);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("transfer emri alındı ve işlem sırasıne eklendi.");

    }

    


}


