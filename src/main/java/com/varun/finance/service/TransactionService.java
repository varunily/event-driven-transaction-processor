package com.varun.finance.service;

import com.varun.finance.dto.PaymentRequest;
import com.varun.finance.dto.TransactionRequest;
import com.varun.finance.model.Transaction;
import com.varun.finance.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Locale;
import java.util.UUID;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository repository;
    private final KafkaTemplate<String, Transaction> kafkaTemplate;
    private final boolean kafkaEnabled;

    public TransactionService(
            TransactionRepository repository,
            @Nullable KafkaTemplate<String, Transaction> kafkaTemplate,
            @Value("${app.kafka.enabled:false}") boolean kafkaEnabled
    ) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaEnabled = kafkaEnabled;
    }

    public void process(TransactionRequest request) {

        if (request.getAmount().compareTo(new BigDecimal("10000")) > 0) {
            throw new IllegalArgumentException("Transaction exceeds allowed limit");
        }

        if (repository.existsByTransactionId(request.getTransactionId())) {
            throw new IllegalStateException("Duplicate transaction detected");
        }

        Transaction txn = new Transaction();
        txn.setTransactionId(request.getTransactionId());
        txn.setAccountNumber(request.getAccountNumber());
        txn.setAmount(request.getAmount());
        txn.setTransactionTime(LocalDateTime.now());
        txn.setCurrency("USD");
        txn.setPaymentGateway("LEGACY_API");
        txn.setStatus("VALIDATED");

        repository.save(txn);
        publishValidatedTransaction(txn);
    }

    public Transaction processPayment(PaymentRequest request) {
        if (request.getAmount().compareTo(new BigDecimal("10000")) > 0) {
            throw new IllegalArgumentException("Payment exceeds allowed limit");
        }

        YearMonth expiry = YearMonth.of(request.getExpiryYear(), request.getExpiryMonth());
        if (expiry.isBefore(YearMonth.now())) {
            throw new IllegalArgumentException("Card expiry date is in the past");
        }

        if (repository.existsByPaymentToken(request.getPaymentToken())) {
            throw new IllegalStateException("Duplicate payment token detected");
        }

        String transactionId = generateTransactionId();
        String cardNumber = request.getCardNumber();
        String cardLast4 = cardNumber.substring(cardNumber.length() - 4);

        Transaction txn = new Transaction();
        txn.setTransactionId(transactionId);
        txn.setPaymentToken(request.getPaymentToken());
        txn.setCardHolderName(request.getCardHolderName());
        txn.setCardLast4(cardLast4);
        txn.setAmount(request.getAmount());
        txn.setCurrency(request.getCurrency().toUpperCase(Locale.ROOT));
        txn.setTransactionTime(LocalDateTime.now());
        txn.setPaymentGateway("FRONTEND_GATEWAY");
        txn.setStatus("TOKENIZED");

        Transaction saved = repository.save(txn);
        publishValidatedTransaction(saved);
        return saved;
    }

    private String generateTransactionId() {
        String candidate = "PAY-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase(Locale.ROOT);
        if (repository.existsByTransactionId(candidate)) {
            return generateTransactionId();
        }
        return candidate;
    }

    private void publishValidatedTransaction(Transaction transaction) {
        if (!kafkaEnabled || kafkaTemplate == null) {
            return;
        }

        try {
            kafkaTemplate.send("validated-transactions", transaction);
        } catch (Exception ex) {
            log.warn("Kafka publish failed for transaction {}", transaction.getTransactionId(), ex);
        }
    }
}
