# Book Rental System

This repository contains a Book Rental System built with a microservices architecture. The system includes two services: Inventory Service and Rental Service, both developed using Spring Boot, Java 17, and various other technologies to ensure a robust, maintainable, and scalable application.

## Table of Contents

- About the Project
- Technologies and Tools
- Architecture
- Setup and Installation
- Running the Services
- Endpoints
- Testing
- Error Handling
- Kafka Integration
- Contributing

## About the Project

The Book Rental System allows users to manage the inventory of books and track rented books. This microservice architecture ensures separation of concerns, allowing independent scaling and development of each service.

## Technologies and Tools

- Java 17
- Spring Boot (version 3.1.4)
  - Spring Web
  - Spring Data JPA
  - Spring Data MongoDB
  - Spring Kafka
- MapStruct for DTO mapping
- Lombok for reducing boilerplate code
- Jakarta Validation for input validation
- PostgreSQL for relational data storage
- MongoDB for NoSQL data storage
- Flyway for database migration
- Docker & Docker Compose for containerization and orchestration
- JUnit 5 & Mockito for testing
- Swagger/OpenAPI for API documentation

## Architecture

The system consists of two microservices:

- Inventory Service: Manages the book inventory using MongoDB as the data store.
- Rental Service: Tracks book rentals, stores rental information in a PostgreSQL database, and integrates with Kafka for messaging.

### Communication Between Services

The services use RESTful APIs for communication and integrate with Kafka for event-driven messaging.

## Setup and Installation

### Prerequisites

- Java 17 installed
- Docker and Docker Compose installed
- Maven installed for building the project

Build the project using Maven:

```agsl
mvn clean install
```

Start the services using Docker Compose:

```agsl
docker-compose up --build
```

## Running the Services

After running Docker Compose, the services will be available on the following ports:

- Inventory Service: http://localhost:8080
- Rental Service: http://localhost:8081

You can access the Swagger UI for each service to explore and test the APIs:

- Inventory Service: http://localhost:8080/swagger-ui.html
- Rental Service: http://localhost:8081/swagger-ui.html

## Endpoints

Inventory Service
- GET /books - Fetch all books
- POST /books - Add a new book
- GET /books/{isbn} - Get book details by ISBN
Rental Service
- GET /book/rented - Fetch all rented books
- GET /book/rented-book - Fetch rented book details by ISBN
- POST /book/rent - Rent a book


## Error Handling
Each service implements global exception handling using @RestControllerAdvice. Errors are returned in a standard JSON format:

```json
{
  "message": "Error description",
  "status": 404
}
```

### Custom Exceptions
- BookNotFoundException: Thrown when a book is not found in the database.
- Runtime exceptions are handled globally, ensuring consistent error responses across services.

### Kafka Integration
The Rental Service listens to the book-rented topic to track book rentals. This integration allows for a decoupled, event-driven approach to managing book rentals.

## Database Optimization with Indexing

In both inventory-service (MongoDB) and rental-service (PostgreSQL), indexing has been implemented on certain fields to optimize query performance, especially for frequent operations:

- MongoDB (inventory-service):
  - An index has been added to the isbn field in the Book collection. This allows for faster searches and retrieval of books based on their ISBN.

- PostgreSQL (rental-service):
  - The isbn column in the rented_book table has been indexed using:
  - CREATE INDEX idx_rented_book_isbn ON rented_book (isbn);
  - This index is created via a Flyway migration script to ensure the database schema remains consistent and version-controlled.

## Future Improvements
- Implementing Full-Text Search:

  - Enhance the inventory-service and rental-service with a full-text search feature, allowing users to search for books not just by isbn but also by title, author, and other metadata. This can be implemented using MongoDB's full-text indexing capabilities or PostgreSQL's tsvector for natural language searches. Full-text search would provide a more flexible and user-friendly way to find books, especially when dealing with large inventories.
- Caching Frequently Accessed Data:

  - Introduce caching mechanisms (e.g., Redis) to store frequently accessed data such as book details, reducing the need to query the database each time. This can significantly enhance application performance, especially in high-traffic scenarios. Additionally, implementing a cache invalidation strategy will ensure that the cached data remains consistent with the database.
- Database Sharding and Replication:

  - To further enhance scalability and reliability, explore sharding for the MongoDB database and replication for PostgreSQL. Sharding would allow horizontal scaling of the inventory-service, distributing data across multiple nodes to handle larger datasets and higher query loads. Replication in PostgreSQL would ensure high availability, providing data redundancy and failover support, which is crucial for maintaining service continuity.

- Implement Book Borrower History with Pagination:
  - Add an endpoint to track the history of borrowers for a given book, which could include details like the borrower’s name and rental period. To handle potential large volumes of data, incorporate pagination in this history view. This would provide both a comprehensive rental history and ensure optimal performance when querying the data.

- Implement Integration Tests Using Testcontainers:
  - Introduce integration tests using Testcontainers to verify the interaction between multiple components of the system, such as the interaction between inventory-service, rental-service, and the databases (both SQL and NoSQL). This will allow testing in an environment that closely mimics production by spinning up real instances of databases in isolated Docker containers. This approach ensures that the system behaves correctly as a whole, catching integration issues early and improving overall system reliability.