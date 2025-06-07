# AIerview - Backend

**AIerview** is an interactive application that simulates technical interviews for developers using **artificial intelligence** to ask questions, analyze answers, and provide real-time feedback.

Perfect for those looking to better prepare for tech industry hiring processes, AIerview delivers a realistic and personalized experience, helping users build confidence and identify areas for improvement.

---

## 📦 Project Modules

The project follows a modular architecture (**Clean Architecture**) and is structured into the following Maven modules:

- `domain` – Contains core business entities and rules
- `application` – Coordinates business use cases
- `infra` – Infrastructure implementations (e.g., database, external APIs)

---

## ⚙️ Technologies Used

The project leverages a modern backend stack with best practices and architectural patterns:

### 🔧 Core Technologies
- **Java 21**
- **Spring Boot 3.4.5**
- **Maven (multi-module project)**
- **Clean Architecture** for modular and maintainable code
- **Lombok** – Reduces boilerplate with annotations

### 📡 Messaging & Async
- **Apache Kafka** – Used for asynchronous feedback generation through event-driven communication
- **Kafka Listener (Spring for Apache Kafka)** – For consuming messages and generating feedback in background

### 🧠 Artificial Intelligence
- **Gemini AI (Google)** – Integrated for feedback generation based on user answers

### 🗄️ Database
- **MongoDB** – NoSQL document database used for persisting questions and answers
- **Spring Data MongoDB** – Simplified MongoDB access with repository pattern

---

## 🚀 How to Run the Project

### Prerequisites
- Java 21
- Maven 3.8+
- IDE (IntelliJ IDEA recommended)

### Steps

```bash
# Clone the repository
git clone https://github.com/aierview/backend.git

# Navigate into the project
cd backend

# Build and install the modules
mvn clean install

# Run the application from the main module (infra)
