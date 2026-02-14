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

## Deploy Online (Render)
1. Push this repo to GitHub.
2. In Render, create a **PostgreSQL** service.
3. Create a **Web Service** from this repo with **Dockerfile**.
4. Set environment variables in Render:
   - `DB_HOST` = your Render Postgres host
   - `DB_PORT` = `5432`
   - `DB_NAME` = your database name
   - `DB_USER` = your database user
   - `DB_PASSWORD` = your database password
   - `APP_KAFKA_ENABLED` = `false` (or `true` only if you provision Kafka)
5. Deploy, then open: `https://<your-render-domain>/payment.html`
