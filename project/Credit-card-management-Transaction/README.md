# Credit Card Management System

A Java-based fintech application for managing customers, credit cards, merchants, purchases, bill payments, and transaction history. It is designed as a hands-on enterprise project using Java, Spring Boot, REST APIs, Spring Data JPA, and Oracle Database.

## Features

- Customer registration, retrieval, update, and deletion
- Credit-card issuance, updates, retrieval, and block/unblock controls
- Merchant registration and management
- Purchase processing with available-credit validation
- Full and partial bill payments
- Complete purchase and payment transaction history, including failed transactions
- Oracle-backed persistence with entity relationships and transactional updates

## Business Rules

- Card numbers must be unique.
- A customer can own multiple credit cards.
- Purchases can be made only with `ACTIVE` cards.
- A purchase cannot make available credit negative.
- A payment cannot exceed the outstanding balance.
- Blocked cards cannot make transactions.
- Every purchase and payment, including failures, is recorded.

## Technology Stack

| Area | Technology |
| --- | --- |
| Language | Java SE 25 |
| Framework | Spring Boot 3.x |
| APIs | Spring Web / REST |
| Data access | Spring Data JPA |
| Database | Oracle Database 26ai |
| Build tool | Maven |
| Architecture | Controller → Service → Repository → Database |

## Domain Model

| Entity | Key fields | Relationship |
| --- | --- | --- |
| `CUSTOMER` | `customer_id`, name, email, mobile number, PAN | One customer can have many credit cards. |
| `CREDIT_CARD` | `card_number`, type, limits, balance, expiry, status | Belongs to a customer and has many transactions. |
| `MERCHANT` | `merchant_id`, name, category, location | Used by purchase transactions. |
| `TRANSACTION` | `transaction_id`, card, merchant, type, amount, timestamp, status | Belongs to a credit card; merchant is required for purchases. |

Transaction types are `PURCHASE` and `PAYMENT`. Transaction statuses are `SUCCESS` and `FAILED`.

## API Endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/customers` | Register a customer |
| GET | `/api/customers/{customerId}` | Get a customer by ID |
| GET | `/api/customers` | List customers |
| PUT | `/api/customers/{customerId}` | Update a customer |
| DELETE | `/api/customers/{customerId}` | Delete a customer |
| POST | `/api/cards` | Issue a credit card |
| GET | `/api/cards/{cardNumber}` | Get a card by number |
| GET | `/api/cards` | List credit cards |
| PUT | `/api/cards/{cardNumber}` | Update a card |
| PUT | `/api/cards/{cardNumber}/block` | Block a card |
| PUT | `/api/cards/{cardNumber}/unblock` | Unblock a card |
| POST | `/api/register/merchant` | Add a merchant |
| GET | `/api/merchants/me` | Get the current merchant |
| GET | `/api/admin/merchants` | List merchants |
| PUT | `/api/merchants/update` | Update a merchant |
| DELETE | `/api/merchants/{merchantId}` | Delete a merchant |
| POST | `/api/transactions/purchase` | Process a card purchase |
| POST | `/api/transactions/payment` | Process a bill payment |
| GET | `/api/transactions/{transactionId}` | Get a transaction by ID |
| GET | `/api/transactions/card/{cardNumber}` | List a card’s transactions |

## Setup

1. Install Java 25, Maven, and Oracle Database 26ai.
2. Create an Oracle schema and the `CUSTOMER`, `CREDIT_CARD`, `MERCHANT`, and `TRANSACTION` tables.
3. Configure the database connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/FREEPDB1
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

4. Build and run the application:

```bash
mvn clean install
mvn spring-boot:run
```

The service will normally be available at `http://localhost:8080`.

## Suggested Project Structure

```text
src/main/java/.../
├── controller/     # REST endpoints
├── service/        # Business rules and transactions
├── repository/     # Spring Data JPA repositories
├── entity/         # JPA entities
├── dto/            # Request and response objects
└── exception/      # Custom exceptions and global error handling
```

## Team Responsibilities

| Team member | Areas |
| --- | --- |
| Arnav | Project setup, customer module, credit-card module, customer validation |
| Anusha | Oracle configuration, merchant module, purchase/payment processing, card validation |
| Swarali | Project structure, transaction module, exception handling, transaction validation |

## Future Architecture

The application can be split into Customer, Credit Card, Merchant, and Transaction services, with Eureka Server providing service discovery.

## License

For educational use.
