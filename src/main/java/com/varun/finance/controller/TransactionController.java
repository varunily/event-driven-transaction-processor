package com.varun.finance.controller;

import com.varun.finance.dto.TransactionRequest;
import com.varun.finance.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody TransactionRequest request) {
        service.process(request);
        return ResponseEntity.ok("Transaction processed successfully");
    }
}