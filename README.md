
# NBC DevOps Transaction - Pre-assignment

## Overview
This project is a simple Spring Boot service designed to manage transaction submissions. It exposes a REST API endpoint:

POST /nbc-bank/devops/v1/transactions

### Features:
- Accepts JSON payloads representing bank transactions.
- Stores transactions in a MySQL database.
- Rejects duplicate transactions based on the `reference` field.

You can also view and manage the deployment via ArgoCD here:  
https://192.168.59.102:30516/applications/argocd/nginx-app?view=tree&resource=

---

## Quick Start (Docker)

### Prerequisites
- Docker installed
- Docker Compose installed

### Steps
1. Clone the repository:
   ```
   git clone <repository-url>
   cd <repository-folder>
   ```
2. Build and start the service with MySQL using Docker Compose:
   ```
   docker-compose up --build
   ```
3. Access the service at:
   ```
   http://localhost:8080
   ```

---

## Docker Compose Configuration

Create a `docker-compose.yml` file in the project root with the following content:

```
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: nbc_devops_mysql
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: nbcDevops
      MYSQL_USER: devops
      MYSQL_PASSWORD: devops123
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  nbc-devops-service:
    build: .
    container_name: nbc_devops_service
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/nbcDevops?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
      SPRING_DATASOURCE_USERNAME: devops
      SPRING_DATASOURCE_PASSWORD: devops123
    depends_on:
      - mysql

volumes:
  mysql_data:
```

- This setup runs MySQL and the Spring Boot service together.
- The service automatically connects to MySQL using the provided environment variables.

---

## API Usage

### Endpoint
```
POST /nbc-bank/devops/v1/transactions
Content-Type: application/json
```

### Sample JSON Payload
```
{
  "service": "DEVOPS_INTERVIEW",
  "name": "NBC BANK TANZANIA",
  "amount": "103,000",
  "account": "AC1234567890",
  "reference": "REF0987654321"
}
```

### Test with curl
```
curl -X POST http://127.0.0.1:8080/nbc-bank/devops/v1/transactions   -H "Content-Type: application/json"   -d '{
    "service":"DEVOPS_INTERVIEW",
    "name":"NBC BANK TANZANIA",
    "amount":"103,000",
    "account":"AC1234567890",
    "reference":"REF0987654321"
  }'
```

- **Success:** Transaction is stored in the MySQL database.
- **Failure:** If the `reference` already exists, the service rejects the duplicate.

---

## Configuration

### MySQL Connection (Optional)
If you want to connect to an external MySQL instead of the Docker container, update `src/main/resources/application.properties`:

```
spring.datasource.url=jdbc:mysql://<HOST>:<PORT>/<DB_NAME>?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=<MYSQL_USER>
spring.datasource.password=<MYSQL_PASSWORD>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

- `spring.jpa.hibernate.ddl-auto=update` ensures the table is created automatically on startup.

---

## Notes
- Ensure MySQL is running before starting the Spring Boot service if you are not using Docker Compose.
- The ArgoCD link provides a UI to monitor the deployment and resources of the application in a Kubernetes cluster.