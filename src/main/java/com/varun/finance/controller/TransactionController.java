package com.varun.finance.controller;

import com.varun.finance.dto.PaymentRequest;
import com.varun.finance.dto.PaymentResponse;
import com.varun.finance.dto.TransactionRequest;
import com.varun.finance.model.Transaction;
import com.varun.finance.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody TransactionRequest request) {
        service.process(request);
        return ResponseEntity.ok("Transaction processed successfully");
    }

    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        Transaction transaction = service.processPayment(request);
        PaymentResponse response = new PaymentResponse(
                transaction.getTransactionId(),
                transaction.getPaymentToken(),
                transaction.getStatus(),
                "Payment token generated and stored successfully"
        );

        return ResponseEntity.ok(response);
    }
}
