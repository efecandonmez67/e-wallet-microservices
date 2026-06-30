package com.efecandonmez.account_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferMessage {

    private Long senderId;
    private Long receiverId;
    private BigDecimal amount;
}
