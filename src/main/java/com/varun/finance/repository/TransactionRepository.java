package com.varun.finance.repository;

import com.varun.finance.model.Transaction;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;

@Repository
public class TransactionRepository {

    private final DynamoDbTable<Transaction> table;

    public TransactionRepository(DynamoDbTable<Transaction> table) {
        this.table = table;
    }

    public boolean existsByTransactionId(String transactionId) {
        if (transactionId == null || transactionId.isBlank()) {
            return false;
        }
        Transaction item = table.getItem(r -> r.key(k -> k.partitionValue(transactionId)));
        return item != null;
    }

    public boolean existsByPaymentToken(String paymentToken) {
        if (paymentToken == null || paymentToken.isBlank()) {
            return false;
        }
        DynamoDbIndex<Transaction> index = table.index("payment_token_index");
        QueryConditional condition = QueryConditional.keyEqualTo(Key.builder().partitionValue(paymentToken).build());
        SdkIterable<Page<Transaction>> results = index.query(r -> r.queryConditional(condition).limit(1));
        return results.stream().anyMatch((Page<Transaction> page) -> !page.items().isEmpty());
    }

    public Transaction save(Transaction transaction) {
        table.putItem(transaction);
        return transaction;
    }
}
