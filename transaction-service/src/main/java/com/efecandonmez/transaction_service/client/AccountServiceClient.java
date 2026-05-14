package com.efecandonmez.transaction_service.client;

import com.efecandonmez.transaction_service.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name ="account-service")
public interface AccountServiceClient {

    @GetMapping("/api/v1/accounts/{id}")
    ResponseEntity<AccountDto> getAccountById(@PathVariable("id") Long userId);

    @PutMapping("/api/v1/accounts/{id}/balance")
    ResponseEntity<Void> updateBalance(@PathVariable("id") Long userId, @RequestParam("amount")BigDecimal amount);


}
