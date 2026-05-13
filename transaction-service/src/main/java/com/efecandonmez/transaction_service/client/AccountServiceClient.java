package com.efecandonmez.transaction_service.client;

import com.efecandonmez.transaction_service.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("account-service")
public interface AccountServiceClient {

    @GetMapping("/v1/account/{id}")
    ResponseEntity<AccountDto> getAccountById(@PathVariable("id") Long id);
}
