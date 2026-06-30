package com.efecandonmez.transaction_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundMessage {

    private Long senderId;
    private BigDecimal amount;
    private String reason;
    private LocalDateTime timestamp;
}
