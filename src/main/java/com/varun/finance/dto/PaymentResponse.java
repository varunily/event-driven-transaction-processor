package com.varun.finance.dto;

public class PaymentResponse {
    private final String transactionId;
    private final String paymentToken;
    private final String status;
    private final String message;

    public PaymentResponse(String transactionId, String paymentToken, String status, String message) {
        this.transactionId = transactionId;
        this.paymentToken = paymentToken;
        this.status = status;
        this.message = message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
