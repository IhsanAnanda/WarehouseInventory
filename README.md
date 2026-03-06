# Warehouse Microservices Project

This project simulates a backend system for a warehouse application using a microservices architecture. It demonstrates handling, processing, and caching of data using a combination of Spring Boot, Apache Kafka, Redis, and Kubernetes.

All inter-service communication flows through the API Gateway, while individual services manage their dedicated data domains (`item`, `inventory`, `order`) asynchronously via Kafka messaging.

## Technology Stack

- **Java 17**
- **Spring Boot 2.7.9**
  - **Spring Web**
  - **Spring Data JPA**
  - **Spring Kafka**
  - **Spring Data Redis**
- **Apache Kafka** (Message Broker)
- **Zookeeper** (Kafka coordination)
- **Redis** (Data caching for faster retrievals)
- **MySQL** (Relational Database)
- **Docker & Docker Compose**
- **Kubernetes** (Container Orchestration)
- **Kafdrop** (Kafka Web UI for topic and message observation)

## Microservices Architecture

1. **API Gateway (`gateway`)**: The single entry point for all external requests. It publishes requests to Kafka topics and listens to response topics.
2. **Item Service (`item`)**: Manages the `item` table (CRUD operations). Integrates with Redis to cache item lists for faster repeated access to `/getItem`.
3. **Inventory Service (`inventory`)**: Manages the `inventory` table.
4. **Order Service (`order`)**: Manages the `order` table.

---

## Prerequisites

Before running the application, ensure you have the following installed on your machine:

- **Java 17 (JDK)**
- **Maven**
- **Docker Desktop** (with Kubernetes enabled)
- **MySQL Server** (running locally on port `3306`)

### Database Table Preparation

You must have a MySQL database named `testDb` running locally on port `3306`.
> Note: The microservices use `spring.jpa.hibernate.ddl-auto=update`, which will automatically generate the database tables for you as long as the database schema `testDb` exists.

---

## Deployment & Running on Kubernetes

This project is configured to run fully orchestrated within a local Kubernetes cluster using Docker Desktop.

### 1. Build the Docker Images

You must build the target images locally so Kubernetes can pull them from your local Docker registry. Navigate to each microservice directory and run the Maven build, followed by the Docker build.

#### Gateway

```bash
cd gateway
mvn clean package -DskipTests
docker build -t warehouse-gateway:latest .
cd ..
```

#### Item

```bash
cd item
mvn clean package -DskipTests
docker build -t warehouse-item:latest .
cd ..
```

#### Inventory

```bash
cd inventory
mvn clean package -DskipTests
docker build -t warehouse-inventory:latest .
cd ..
```

#### Order

```bash
cd order
mvn clean package -DskipTests
docker build -t warehouse-order:latest .
cd ..
```

### 2. Deploy Infrastructure Services

Before deploying the Spring Boot applications, you must deploy Kafka, Zookeeper, Kafdrop, and Redis to the `warehouse` namespace.

```bash
# Deploy Kafka, Zookeeper, and Kafdrop
kubectl apply -f warehouse-kafka-deploytment.yaml -n warehouse

# Deploy Redis (used by the Item service for caching)
kubectl apply -f redis-deployment.yaml -n warehouse
```

> Wait a few moments to ensure the `kafka` and `zookeeper` pods are in `Running` status. Use `kubectl get pods -n warehouse` to verify.

### 3. Deploy the Microservices

Once the underlying infrastructure is running, deploy the four microservices.

```bash
kubectl apply -f gateway/deployment.yaml -n warehouse
kubectl apply -f item/deployment.yaml -n warehouse
kubectl apply -f inventory/deployment.yaml -n warehouse
kubectl apply -f order/deployment.yaml -n warehouse
```

### 4. Port Forwarding / Accessing the Application

Since the API Gateway is the central entry point configured as a `LoadBalancer` Service, Docker Desktop automatically exposes it to your `localhost`.

- **API Gateway Base URL**: `http://localhost:8090`
- **Kafdrop Dashboard Base URL**: `http://localhost:9000` (Use this to view live Kafka messages in the browser)

---

## Understanding the Kafka Asynchronous Flow

Because of the asynchronous nature of this stack, every HTTP request sent to the API gateway is converted into a Kafka message payload on a specific topic. The domain service processes the request and replies on a distinct "response" topic.

### Example: The `/getInventory` Flow

Here is the step-by-step lifecycle of how data moves from the Gateway to Kafka to the Inventory service when a client requests inventory data:

1. **Client Request**: A client sends an HTTP POST request to `http://localhost:8090/getInventory` (the API Gateway).
2. **Gateway Produces**: The `gateway` service receives the request, wraps the payload in a DTO, and acts as a Kafka Producer. It publishes this message to the `get-inventory-request` Kafka topic.
3. **Gateway Waits**: The `gateway` opens a temporary thread waiting for a correlation response.
4. **Inventory Consumes**: The `inventory` service, constantly listening as a Kafka Consumer on the `get-inventory-request` topic, detects and pulls the new message.
5. **Database Query**: The `inventory` service executes the business logic, querying the `inventory` table in the MySQL Database (`host.docker.internal:3306`).
6. **Inventory Produces**: The `inventory` service formats the database result and publishes the final payload to the `get-inventory-response` Kafka topic.
7. **Gateway Consumes & Responds**: The `gateway` detects the message on the `get-inventory-response` topic, correlates it back to the original waiting HTTP thread, and returns the final JSON response to the client.

> **Observation Tip**: You can trace all this activity, view the exact JSON payloads, and see the topic partitions in real-time by observing the **Kafdrop UI** at `http://localhost:9000`.

---

### Example: The `/getItem` Flow (with Redis)

This flow is similar to the above, but includes our caching layer:

1. The `gateway` publishes to `list-item-request`.
2. The `item` service consumes the message.
3. The `item` service hits the **Redis cache** first. If the data is missing (cache miss), it queries the MySQL DB and then saves the result to Redis.
4. The `item` service pushes the payload to the `list-item-response` topic.
5. The `gateway` detects the response and returns it to the client.
