package com.varun.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionRequest {
    @NotBlank
    private String transactionId;

    @NotBlank
    private String accountNumber;

    @Positive
    private BigDecimal amount;
}