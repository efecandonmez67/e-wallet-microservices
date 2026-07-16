# 💳 E-Wallet Microservices Backend

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) ![PostgreSQL](https://img.shields.io/badge/postgresql-4169e1?style=for-the-badge&logo=postgresql&logoColor=white) ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white) ![RabbitMQ](https://img.shields.io/badge/Rabbitmq-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white) ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

A modern, high-performance microservices architecture for an e-wallet platform. Built with a focus on optimal service discovery, secure API communication, asynchronous transaction processing, and a scalable backend infrastructure.

---

## ✨ Key Features

*   **Microservices Architecture:** Fully decoupled services (`auth-service`, `account-service`, `transaction-service`) registered and routed via **Eureka Server** and **API Gateway**.
*   **Secure Authentication:** JWT-based user authentication and secure, stateless endpoint protection handled seamlessly through the gateway.
*   **Asynchronous Processing:** Utilizes **RabbitMQ** for event-driven communication, ensuring reliable and non-blocking financial transactions.
*   **High-Speed Caching:** Integrated **Redis** to optimize data fetching and reduce database load for frequently accessed wallet states.
*   **Containerized Infrastructure:** One-click deployment for databases, message brokers, and caches using `docker-compose`.

---

## 💻 Tech Stack

**Backend Core**
*   **Core:** Java 17+, Spring Boot 3, Spring Cloud
*   **Database:** PostgreSQL & Spring Data JPA / Hibernate
*   **Caching:** Redis
*   **Message Broker:** RabbitMQ
*   **Security:** Spring Security & JWT

**Infrastructure & Deployment**
*   **Service Discovery:** Netflix Eureka
*   **Gateway:** Spring Cloud Gateway
*   **Containerization:** Docker & Docker Compose

---

## 🚀 Quick Start (Local Development)

If you want to run this project locally, follow these steps:

### 1. Infrastructure Setup (Docker)
Navigate to the root directory where the `docker-compose.yml` is located and start the required background services (PostgreSQL, Redis, RabbitMQ):

```bash
docker-compose up -d
```
### 2. Microservices Setup
Open the project in your IDE (IntelliJ IDEA / Eclipse) and start the Spring Boot services strictly in the following order to ensure proper registration:

1. `eureka-server` *(Wait for it to start completely)*
2. `api-gateway`
3. `auth-service`
4. `account-service` & `transaction-service`

Once all services are up and running, all client requests should be directed to the API Gateway at `http://localhost:8080`.
