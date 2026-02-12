package com.varun.finance.service;

import com.varun.finance.dto.TransactionRequest;
import com.varun.finance.model.Transaction;
import com.varun.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public void process(TransactionRequest request) {

        if (request.getAmount().compareTo(new BigDecimal("10000")) > 0) {
            throw new IllegalArgumentException("Transaction exceeds allowed limit");
        }

        if (repository.existsByTransactionId(request.getTransactionId())) {
            throw new IllegalStateException("Duplicate transaction detected");
        }

        Transaction txn = Transaction.builder()
                .transactionId(request.getTransactionId())
                .accountNumber(request.getAccountNumber())
                .amount(request.getAmount())
                .transactionTime(LocalDateTime.now())
                .status("VALIDATED")
                .build();

        repository.save(txn);
        kafkaTemplate.send("validated-transactions", txn);
    }
}