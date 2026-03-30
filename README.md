# 🛒 E-commerce API (Work in Progress)

A RESTful API built with **Kotlin + Spring** for managing customers, orders, and order items. Features full CRUD operations, business rule validation, paginated responses, and a clean layered architecture.
 
---

## 🧱 Tech Stack

| Layer | Technology                  |
|---|-----------------------------|
| Language | Kotlin 2.2.21               |
| Framework | Spring Boot 4.0.3           |
| Persistence | Spring Data JPA + Hibernate |
| Validation | Jakarta Bean Validation     |
| Build Tool | Gradle (Kotlin DSL)         |
| Java Version | 21                          |
 
---

## 📁 Project Structure

```
src/main/kotlin/com/renzozukeram/ecommerce/
├── controllers/
│   ├── CustomerController.kt
│   ├── OrderController.kt
│   └── OrderItemController.kt
├── entities/
│   ├── Customer.kt
│   ├── Order.kt
│   ├── OrderItem.kt
│   └── OrderStatus.kt
├── exceptions/
│   ├── Exceptions.kt
│   ├── GlobalExceptionHandler.kt
├── mappers/
│   ├── CustomerMapper.kt
│   ├── OrderMapper.kt
│   └── OrderItemMapper.kt
├── model/
│   ├── dto/
│   │   ├── CustomerDto.kt
│   │   ├── OrderDto.kt
│   │   └── OrderItemDto.kt
│   └── entities/
│       ├── Customer.kt
│       ├── Order.kt
│       ├── OrderItem.kt
│       └── OrderStatus.kt
├── repositories/
│   ├── CustomerRepository.kt
│   ├── OrderRepository.kt
│   └── OrderItemRepository.kt
└── services/
    ├── CustomerService.kt
    ├── OrderService.kt
    └── OrderItemService.kt

src/main/resources/
└── application.properties
```
 
---

## 🗃️ Data Model

```
Customer (1) ──────< Order (1) ──────< OrderItem
```

- A **Customer** can have many **Orders**
- An **Order** can have many **OrderItems**
- Deleting a customer cascades to all their orders and items (`CascadeType.ALL` + `orphanRemoval`)

### OrderStatus state machine

Only the following transitions are allowed:

```
PENDING ──→ PROCESSING ──→ SHIPPED ──→ DELIVERED
   └──────────────────────────────→ CANCELLED
                   └──────────────→ CANCELLED
```

Attempting an invalid transition returns `422 Unprocessable Entity`.
 
---

## ⚙️ Configuration

### Prerequisites

- Java 21+
- Gradle 8+

### Running the application

```bash
./gradlew bootRun
```

The API will start at `http://localhost:8080`.
 
---

## 🌐 API Endpoints

Base URL: `/api/v1`

### Customers

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/customers` | List all customers (paginated) |
| `GET` | `/customers/{id}` | Get customer by ID |
| `GET` | `/customers/{id}/orders` | Get customer with their orders |
| `POST` | `/customers` | Create a new customer |
| `PUT` | `/customers/{id}` | Update a customer |
| `DELETE` | `/customers/{id}` | Delete a customer |

### Orders

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/orders` | List all orders (paginated) |
| `GET` | `/orders/{id}` | Get order by ID |
| `GET` | `/orders/customer/{customerId}` | List orders by customer |
| `GET` | `/orders/status/{status}` | List orders by status |
| `POST` | `/orders` | Create a new order |
| `PATCH` | `/orders/{id}/status` | Update order status |
| `DELETE` | `/orders/{id}` | Delete an order |

### Order Items

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/orders/{orderId}/items` | List items of an order |
| `GET` | `/order-items/{id}` | Get item by ID |
| `POST` | `/orders/{orderId}/items` | Add item to an order |
| `PUT` | `/order-items/{id}` | Update an item |
| `DELETE` | `/order-items/{id}` | Remove an item from an order |

> **Note:** Items can only be added, updated, or removed when the order status is `PENDING`.
 
---

## 📦 Request & Response Examples

### Create a Customer

**POST** `/api/v1/customers`

```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "phoneNumber": "+55 84 99999-0000"
}
```

**Response 201**

```json
{
  "id": "a1b2c3d4-...",
  "name": "Jane Doe",
  "email": "jane@example.com",
  "phoneNumber": "+55 84 99999-0000",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```
 
---

### Create an Order

**POST** `/api/v1/orders`

```json
{
  "customerId": "a1b2c3d4-...",
  "items": [
    {
      "productName": "Mechanical Keyboard",
      "quantity": 1,
      "unitPrice": 350.00
    },
    {
      "productName": "USB-C Cable",
      "quantity": 2,
      "unitPrice": 25.00
    }
  ]
}
```

**Response 201**

```json
{
  "id": "e5f6g7h8-...",
  "customerId": "a1b2c3d4-...",
  "customerName": "Jane Doe",
  "orderDate": "2024-01-15T10:31:00",
  "status": "PENDING",
  "totalAmount": 400.00,
  "items": [
    {
      "id": "i9j0k1l2-...",
      "productName": "Mechanical Keyboard",
      "quantity": 1,
      "unitPrice": 350.00,
      "totalPrice": 350.00
    },
    {
      "id": "m3n4o5p6-...",
      "productName": "USB-C Cable",
      "quantity": 2,
      "unitPrice": 25.00,
      "totalPrice": 50.00
    }
  ],
  "createdAt": "2024-01-15T10:31:00"
}
```
 
---

### Update Order Status

**PATCH** `/api/v1/orders/{id}/status`

```json
{
  "status": "PROCESSING"
}
```
 
---

## ❌ Error Responses

All errors follow a consistent format:

```json
{
  "timestamp": "2024-01-15T10:32:00",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with id: a1b2c3d4-..."
}
```

Validation errors return a map of field-level messages:

```json
{
  "timestamp": "2024-01-15T10:32:00",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "email": "Invalid email format",
    "name": "Name is required"
  }
}
```

| HTTP Status | Scenario |
|---|---|
| `400 Bad Request` | Validation error on request body |
| `404 Not Found` | Resource not found |
| `409 Conflict` | Duplicate email on customer |
| `422 Unprocessable Entity` | Invalid status transition or business rule violation |
| `500 Internal Server Error` | Unexpected server error |
 
---

## 🔑 Key Design Decisions

**Read-only transactions by default** — all services are annotated with `@Transactional(readOnly = true)` at class level; only write methods override with `@Transactional`. This improves performance and prevents accidental writes.

**JOIN FETCH to avoid N+1** — repositories use `@Query` with `JOIN FETCH` on endpoints that load associations (e.g., `findByIdWithDetails`, `findByIdWithOrders`).

**Manual mappers over MapStruct** — mappers are plain Spring `@Component` classes, keeping the build simple and the mapping logic explicit and easy to debug.

**`totalAmount` always recalculated** — whenever an item is added, updated, or removed, the order's `totalAmount` is recalculated server-side from the items list. Clients never send this value.

**Status machine enforced in the service layer** — invalid transitions are rejected before any persistence call, returning a clear `422` with a descriptive message.

---

Developed by Renzo Zukeram