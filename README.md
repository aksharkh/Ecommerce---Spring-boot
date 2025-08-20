# Event-Driven Order Processing System

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Build](https://img.shields.io/badge/build-passing-brightgreen)

This project is a simplified, event-driven order processing system built in Java and Spring Boot. It simulates the backend of an e-commerce platform by processing a stream of order-related events from a file, updating the state of orders, and notifying observers of changes.

---

## ## Core Features

* **Event-Driven Architecture:** The system is designed to react to a stream of events read from a JSON file.
* **Comprehensive Event Processing:** Handles four distinct event types: `OrderCreated`, `PaymentReceived`, `ShippingScheduled`, and `OrderCancelled`.
* **State Management:** Persists and updates the state of orders (`PENDING`, `PAID`, `SHIPPED`, etc.) in an in-memory database.
* **Observer Pattern:** Implements a notification system to inform observers (`LoggerObserver`, `AlertObserver`) of significant state changes.
* **Robust Data Modeling:** Uses a proper relational model with `Order` and `OrderItem` entities, linked by a one-to-many relationship.
* **Graceful Error Handling:** Includes logic to handle events for non-existent orders and unknown event types.

---

## ## Architectural Design

This project follows a modern, layered architecture to ensure a clean separation of concerns, making the application testable and maintainable.

* **Service Layer:** An interface-based service (`OrderService`/`OrderServiceImpl`) contains all the core business logic, mirroring professional application design.
* **Repository Layer:** Uses Spring Data JPA (`OrderRepository`) to abstract away all data persistence logic.
* **Entity & DTO Pattern:**
    * **Entities** (`Order`, `OrderItem`) represent the database schema.
    * **DTOs** (`EventDTO`, `OrderDTO`, etc.) are used to transfer data into and out of the service layer, decoupling the business logic from the persistence layer.
* **Dependency Injection:** The application is built on the Spring Framework, making extensive use of dependency injection to manage components and their relationships.

---

## ## Technologies Used

* **Java 17**
* **Spring Boot 3.x**
* **Spring Data JPA**
* **H2 In-Memory Database**
* **Lombok:** To reduce boilerplate code.
* **ModelMapper:** For object-to-object mapping between DTOs and Entities.
* **Gson:** For parsing the input `events.json` file.
* **Maven:** As the build and dependency management tool.

---

## ## How to Run the Application

#### ### Prerequisites
* Java Development Kit (JDK) 17 or newer.
* Apache Maven.

#### ### Steps
1.  **Clone the repository:**
    ```shell
    git clone https://github.com/aksharkh/Ecommerce---Spring-boot.git
    ```
2.  **Navigate to the project directory:**
    ```shell
    cd Ecommerce---Spring-boot
    ```
3.  **Build the project using Maven:**
    This will compile the code and download all necessary dependencies.
    ```shell
    mvn clean install
    ```
4.  **Run the application:**
    You can either run the main class `EcommerceApplication.java` from your IDE or use the command line:
    ```shell
    java -jar target/Ecommerce-0.0.1-SNAPSHOT.jar
    ```

---

## ## How It Works

Upon startup, the application's `CommandLineRunner` is automatically triggered. It performs the following actions:

1.  Reads the `src/main/resources/events.json` file line by line.
2.  Parses each JSON line into a specific `EventDTO` subclass.
3.  Passes the DTO to the `OrderService` for processing.
4.  The service updates the state of the corresponding `Order` entity and its `OrderItem`s in the H2 database.
5.  Finally, all registered `Observer` components are notified of the change.

All output, including logs and alerts, is printed to the console.

---

## ## Viewing the Database (H2 Console)

The application uses an in-memory H2 database. You can view its contents while the application is running.

1.  Open your web browser and navigate to: `http://localhost:8080/h2-console`
2.  Use the following settings to connect:
    * **Driver Class:** `org.h2.Driver`
    * **JDBC URL:** `jdbc:h2:mem:testdb`
    * **User Name:** `sa`
    * **Password:** (leave blank)
3.  Click **Connect**. You will be able to see the `ORDERS` and `ORDER_ITEMS` tables and run SQL queries against them.
