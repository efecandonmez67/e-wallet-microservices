package com.efecandonmez.transaction_service.client;

import com.efecandonmez.transaction_service.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name ="account-service")
public interface AccountServiceClient {

    @GetMapping("/api/v1/accounts/{id}")
    ResponseEntity<AccountDto> getAccountById(@PathVariable("id") Long accountId);

    @PutMapping("/api/v1/accounts/{id}/withdraw")
    ResponseEntity<Void> withDraw(@PathVariable("id") Long accountId, @RequestParam("amount")BigDecimal amount);

    @PutMapping("/api/v1/accounts/{id}/deposit")
    ResponseEntity<Void> deposit(@PathVariable("id") Long accountId, @RequestParam("amount") BigDecimal amount);


}
