package com.efecandonmez.transaction_service.controller;

import com.efecandonmez.transaction_service.model.Transaction;
import com.efecandonmez.transaction_service.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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

    @GetMapping("/history/{accountId}")
    public ResponseEntity<Page<Transaction>> getTransactionHistory(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());

        Page<Transaction> history = transactionService.getTransactionHistory(accountId, pageable);

        return ResponseEntity.ok(history);
    }

    


}


