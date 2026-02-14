CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(255) UNIQUE,
    account_number VARCHAR(255),
    amount NUMERIC(19, 2),
    transaction_time TIMESTAMP,
    status VARCHAR(255)
);

ALTER TABLE transactions ADD COLUMN IF NOT EXISTS payment_token VARCHAR(120);
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS card_holder_name VARCHAR(120);
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS card_last4 VARCHAR(4);
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS currency VARCHAR(3);
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS payment_gateway VARCHAR(60);

CREATE UNIQUE INDEX IF NOT EXISTS ux_transactions_payment_token
    ON transactions (payment_token)
    WHERE payment_token IS NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS ux_transactions_transaction_id
    ON transactions (transaction_id)
    WHERE transaction_id IS NOT NULL;
