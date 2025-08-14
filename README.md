# NBC DevOps Transaction - Pre-assignment

## Overview
Simple Spring Boot service exposing:
POST /nbc-bank/devops/v1/transactions

It accepts the sample JSON payload and stores transactions in MySQL.
Duplicate `reference` values are rejected.

## Quick start (with Docker)
1. Ensure Docker & Docker Compose are installed.
2. Build & start:
   ```
   docker-compose up --build
   ```
3. Service will be available at http://localhost:8080

## Test with curl
Successful request:
```
curl -X POST http://127.0.0.1:8080/nbc-bank/devops/v1/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "service":"DEVOPS_INTERVIEW",
    "name":"NBC BANK TANZANIA",
    "amount":"103,000",
    "account":"AC1234567890",
    "reference":"REF0987654321"
  }'
```

## Notes
- Update `application.properties` if you run MySQL separately.
- The project uses `spring.jpa.hibernate.ddl-auto=update` to create the table automatically.
