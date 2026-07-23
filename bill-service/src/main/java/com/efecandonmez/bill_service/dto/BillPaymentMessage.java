package com.efecandonmez.bill_service.dto;

import java.math.BigDecimal;

public record BillPaymentMessage(Long billId, Long accountId, BigDecimal amount) {}