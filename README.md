# Financial Transaction Processor

Spring Boot microservice for processing financial transactions with validation, Kafka integration, PostgreSQL persistence, and CI/CD automation.

## Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- Kafka
- PostgreSQL
- Docker
- GitHub Actions

## Run Application
mvn clean install
mvn spring-boot:run

## Local Infra (Docker)
- PostgreSQL: run with `-p 5433:5432` (project defaults to DB port `5433`)
- Kafka/Redpanda: run with `-p 9092:9092`

## Payment Page
- Open `http://localhost:8080/payment.html`
- Submit card-holder details and amount
- Frontend generates a payment token and sends it to `POST /api/transactions/payments`
- Backend stores tokenized payment metadata in PostgreSQL (`transactions` table)
