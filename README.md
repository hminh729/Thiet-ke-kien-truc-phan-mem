# Utility Management System - Microservices Architecture

A comprehensive microservices-based system designed for managing utility services including customers, meters, contracts, and automated billing.

## 🚀 Architecture Overview

This project is built using **Spring Cloud** microservices architecture, ensuring scalability, reliability, and ease of maintenance.

### Core Services:
- **Eureka Server**: Service discovery and registration.
- **API Gateway**: Unified entry point for all client requests.
- **User Service**: Identity and Access Management (IAM).
- **Customer Service**: Management of customer profiles and information.
- **Meter Service**: Tracking and monitoring of utility meters.
- **Contract Service**: Handling service agreements and legal contracts.
- **Asset Service**: Managing physical assets and infrastructure.
- **Bill Service**: Automated billing engine and payment tracking.
- **Frontend**: Modern web interface built with **React**, **Vite**, and **TypeScript**.

## 🛠️ Technology Stack

- **Backend**: Java 17+, Spring Boot 3.x, Spring Cloud
- **Database**: MySQL / PostgreSQL (managed per service)
- **Frontend**: React, TypeScript, Vite, Tailwind CSS
- **Containerization**: Docker, Docker Compose
- **Build Tool**: Maven

## 📂 Project Structure

```text
├── API-Gateway/       # Routing and security
├── Asset-Service/     # Asset management
├── Bill-Service/      # Billing logic
├── Contract-Service/  # Contract management
├── Customer-Service/  # Customer data
├── Eureka-Server/     # Service discovery
├── Meter-Service/     # Meter readings
├── User-Service/      # Authentication & Users
├── frontend/          # React application
└── docker-compose.yml # Infrastructure setup
```

## 🚦 Getting Started

### Prerequisites
- JDK 17 or higher
- Node.js & npm
- Docker & Docker Compose
- Maven

### Running with Docker
1. Clone the repository
2. Run the following command:
   ```bash
   docker-compose up -d
   ```

### Running Locally
1. Start the **Eureka Server** first.
2. Start the **API Gateway**.
3. Start individual microservices as needed.
4. Run the frontend:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

## 📝 Author
**Pham Cong Minh** - *Student at PTIT*

---
*Built for the Software Architecture & Design Course (Kien Truc Thiet Ke Phan Mem)*
