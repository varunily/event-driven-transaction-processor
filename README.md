# Financial Transaction Processor

Spring Boot microservice for processing financial transactions with validation, optional Kafka integration, and DynamoDB persistence.

## Tech Stack
- Java 17
- Spring Boot
- DynamoDB (AWS SDK v2)
- Kafka
- Docker
- GitHub Actions

## Run Application
mvn clean install
mvn spring-boot:run

## DynamoDB Setup
Create a DynamoDB table (default name `transactions`) with:
- Partition key: `transactionId` (String)
- Global secondary index: `payment_token_index` with partition key `paymentToken` (String)

Set environment variables:
- `AWS_REGION` (example: `us-east-2`)
- `DYNAMODB_TABLE` (example: `transactions`)
- `APP_KAFKA_ENABLED` = `false` (or `true` only if you provision Kafka)

If you want to use DynamoDB Local, set:
- `DYNAMODB_ENDPOINT` (example: `http://localhost:8000`)

## Payment Page
- Open `http://localhost:8080/payment.html`
- Submit card-holder details and amount
- Frontend generates a payment token and sends it to `POST /api/transactions/payments`
- Backend stores tokenized payment metadata in DynamoDB

## Deploy Online (AWS App Runner)
1. Build and push the Docker image to ECR.
2. Create an App Runner service from the ECR image.
3. Set environment variables:
   - `AWS_REGION`
   - `DYNAMODB_TABLE`
   - `APP_KAFKA_ENABLED`
4. Attach an IAM role to App Runner with:
   - `dynamodb:PutItem`
   - `dynamodb:GetItem`
   - `dynamodb:Query`
   - `dynamodb:DescribeTable`
5. Deploy, then open: `https://<your-app-runner-domain>/payment.html`
