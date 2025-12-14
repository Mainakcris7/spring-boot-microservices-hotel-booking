# Hotel Booking Microservices 🏨

[![Java 21](https://img.shields.io/badge/Java-21-blue)](https://www.oracle.com/java/)
[![Spring Boot 3.5.4](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen)](https://spring.io/projects/spring-boot)
[![Kafka](https://img.shields.io/badge/Kafka-enabled-orange)](https://kafka.apache.org/)
[![Gradle](https://img.shields.io/badge/Build-Gradle-yellowgreen)](https://gradle.org/)
[![H2 DB](https://img.shields.io/badge/Database-H2-lightgrey)](https://www.h2database.com/)

A comprehensive Spring Boot microservices architecture for a hotel room booking system. This project demonstrates modern microservices patterns including service discovery, API gateway, JWT authentication, event-driven architecture with Kafka, and distributed tracing.

## Project Overview 🚀

This hotel booking system is built using a microservices architecture with the following components:

- **Service Discovery**: Eureka Server for dynamic service registration and discovery
- **API Gateway**: Central entry point for all client requests with authentication filtering
- **Authentication**: JWT-based authentication service
- **Business Services**: Hotel, Room, Booking, and Customer management services
- **Event-Driven Architecture**: Kafka-based notification system
- **Observability**: Distributed tracing with Micrometer and Prometheus metrics

### Technology Stack 🧰

- **Language**: Java 21
- **Framework**: Spring Boot 3.5.4
- **Cloud**: Spring Cloud (Eureka, Gateway, OpenFeign)
- **Build Tool**: Gradle
- **Database**: H2 (in-memory)
- **Message Broker**: Apache Kafka
- **Security**: Spring Security + JWT (JJWT)
- **Monitoring**: Prometheus, Micrometer, OpenTelemetry

---

## Microservices 🧩

### 1. API Gateway 🛡️
**Port**: 7777  
**Description**: Central routing gateway that directs all incoming requests to appropriate microservices. Implements authentication filtering and load balancing using Eureka service discovery.

**Routing Rules**:
- `/hotels/**` → Hotel Service
- `/rooms/**` → Room Service
- `/bookings/**` → Booking Service
- `/customers/**` → Customer Service
- `/auth/**` → Auth Service

**Key Features**:
- Request routing with load balancing
- AuthFilter for JWT validation on protected routes
- Virtual threads enabled for improved concurrency
- Monitoring and metrics collection

---

### 2. Auth Service 🔐
**Port**: 1111  
**Description**: Handles user authentication and authorization using JWT tokens. Manages customer registration, login, and token validation.

**Endpoints**:

| HTTP Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register a new customer with email and password |
| POST | `/auth/login` | Authenticate customer and receive JWT token |
| POST | `/auth/validate` | Validate JWT token and user credentials |
| PUT | `/auth/update` | Update customer profile information |

**Key Features**:
- JWT token generation and validation
- Password encoding with Spring Security
- Customer registration and profile management
- Integration with Eureka for service discovery
- Distributed tracing enabled

---

### 3. Booking Service 📅
**Port**: 9090  
**Description**: Manages hotel room bookings. Handles creating, updating, and canceling bookings. Publishes booking events to Kafka for notifications.

**Endpoints**:

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `/bookings` | Retrieve all bookings |
| GET | `/bookings/{id}` | Get booking details by ID |
| POST | `/bookings` | Create a new booking |
| PUT | `/bookings` | Update an existing booking |
| DELETE | `/bookings/{id}` | Delete booking by ID |
| DELETE | `/bookings/room/{id}` | Delete all bookings for a specific room |
| DELETE | `/bookings/customer/{id}` | Delete all bookings for a specific customer |

**Key Features**:
- Booking validation with date checks and availability
- Kafka producer for booking notifications (CREATE and CANCEL events)
- Integration with Hotel and Room services via OpenFeign
- Transaction management for data consistency
- Virtual threads enabled
- Returns BookingConfirmationDto with complete booking details

**Event Publishing**: 
- Publishes `BookingDetails` to Kafka topic (default: "booking") for both CREATE and CANCEL booking types
- Events consumed by Notification Service

---

### 4. Customer Service 👥
**Port**: 6060  
**Description**: Manages customer information. Provides endpoints to retrieve and manage customer data.

**Endpoints**:

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `/customers` | Retrieve all customers |
| GET | `/customers/{id}` | Get customer details by ID |
| DELETE | `/customers/{id}` | Delete customer by ID |

**Key Features**:
- Customer data persistence
- Integration with Eureka for service discovery
- OpenFeign client for inter-service communication
- Distributed tracing and metrics

---

### 5. Eureka Server 🧭
**Port**: 8761  
**Description**: Service registry and discovery server. All microservices register themselves here, enabling dynamic service-to-service communication.

**Key Features**:
- Dynamic service registration and deregistration
- Health check monitoring
- Service discovery for load balancing
- Dashboard available at `http://localhost:8761`

**Configuration**:
- All microservices auto-register with Eureka on startup
- Configured with fetch-registry enabled for service discovery
- IP address preference enabled in instance configuration

---

### 6. Hotel Service 🏨
**Port**: 7070  
**Description**: Manages hotel information including hotel details, addresses, and metadata.

**Endpoints**:

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `/hotels` | Retrieve all hotels |
| GET | `/hotels/{id}` | Get hotel details by ID |
| GET | `/hotels/exists/{id}` | Check if hotel exists (returns boolean) |
| POST | `/hotels` | Create a new hotel |
| PUT | `/hotels` | Update hotel information |
| DELETE | `/hotels/{id}` | Delete hotel by ID |

**Key Features**:
- Hotel CRUD operations with validation
- Address information for each hotel
- OpenFeign integration for inter-service communication
- Availability checks for bookings

---

### 7. Room Service 🛏️
**Port**: 8080  
**Description**: Manages room details within hotels. Handles room creation, updates, and availability management.

**Endpoints**:

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `/rooms` | Retrieve all rooms |
| GET | `/rooms/{id}` | Get room details by ID |
| POST | `/rooms` | Create a new room |
| PUT | `/rooms` | Update room information |
| DELETE | `/rooms/{id}` | Delete room by ID |
| DELETE | `/rooms/hotel/{id}` | Delete all rooms for a specific hotel |

**Request Body Example (POST/PUT)**:
```json
{
  "hotelId": 1,
  "roomType": "SINGLE",
  "pricePerNight": 150.0,
  "checkInTime": "02:00 PM",
  "checkOutTime": "11:00 AM",
  "amenities": ["AC", "WIFI", "FRIDGE"]
}
```

**Key Features**:
- Room type management (SINGLE, DOUBLE, SUITE, etc.)
- Amenity tracking (AC, FRIDGE, WASHING_MACHINE, WIFI)
- Check-in/check-out time configuration
- Price management per room
- Validation for room data

---

### 8. Notification Service 🔔
**Port**: 5050  
**Description**: Event-driven service that consumes booking events from Kafka and sends notifications. Implements email notifications (dummy) for booking confirmations and cancellations.

**Features**:
- Kafka consumer listening to "booking" topic
- Dummy email notification generation for booking creation and cancellation
- Receives `BookingDetails` payload with complete booking information
- Group ID: "consumer-grp-1"

**Event Processing**:
- **CREATE events**: Generate booking confirmation emails
- **CANCEL events**: Generate booking cancellation emails

**Dummy Email Content Includes**:
- Booking ID and type
- Customer name and email
- Hotel name and address
- Room type and details
- Check-in/check-out dates
- Total booking price

---

## Payload/DTO Structure 📦

### Kafka Notification Payload

**Location**: `hotel-booking-microservice-payload/payload`

The shared payload library contains DTOs for Kafka event messaging:

#### BookingDetails
Contains complete booking information sent via Kafka:
```java
@Data
public class BookingDetails {
    private int bookingId;
    private BookingType bookingType;        // CREATE or CANCEL
    private int customerId;
    private String customerName;
    private String customerEmail;
    private String hotelName;
    private String hotelAddress;
    private int roomId;
    private String roomType;
    private double totalPrice;
    private LocalDate bookedFrom;
    private LocalDate bookedUntil;
}
```

#### BookingType
Enum for event types:
- `CREATE` - Booking created event
- `CANCEL` - Booking cancelled event

---

## Project Setup and Installation ⚙️

### Prerequisites

- **Java 21** or higher
- **Gradle 8.x** or higher
- **Apache Kafka** (latest version)
- **Git**

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd spring-boot-microservices-hotel-booking
```

### Step 2: Install Payload to Local Maven Repository

The project uses a shared payload library (`com.mainak:payload:1.0-SNAPSHOT`) for Kafka DTOs. This must be installed to your local Maven repository before building the microservices.

**Navigate to the payload folder**:
```bash
cd hotel-booking-microservice-payload/payload
```

**Install to local .m2 repository**:
```bash
mvn clean install
```

This command will:
- Clean any previous builds
- Compile the payload module
- Create JAR file
- Install to `~/.m2/repository/com/mainak/payload/1.0-SNAPSHOT/`

**Verify Installation**:
Check that the following directory exists:
```
~/.m2/repository/com/mainak/payload/1.0-SNAPSHOT/payload-1.0-SNAPSHOT.jar
```

### Step 3: Configure Kafka

**Start Kafka Server** (if not already running):

Using Docker (recommended):
```bash
docker run --name kafka -d -p 9092:9092 -e KAFKA_BROKER_ID=1 \
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  confluentinc/cp-kafka:latest
```

Or using local installation, start Zookeeper and Kafka:
```bash
# Terminal 1 - Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Terminal 2 - Start Kafka
bin/kafka-server-start.sh config/server.properties
```

**Create Kafka Topic**:
```bash
kafka-topics --create --topic booking --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

### Step 4: Build Microservices

Navigate to the microservices directory:
```bash
cd microservices
```

Build all services using Gradle:
```bash
./gradlew clean build
```

Or build individual services:
```bash
cd eureka_server/eureka_server && ./gradlew clean build
cd ../auth-service/auth-service && ./gradlew clean build
cd ../booking_service/booking_service && ./gradlew clean build
cd ../customer-service/customer-service && ./gradlew clean build
cd ../hotel_service/hotel_service && ./gradlew clean build
cd ../room_service/room_service && ./gradlew clean build
cd ../notification_service/notification_service && ./gradlew clean build
cd ../api_gateway/api_gateway && ./gradlew clean build
```

### Step 5: Run Microservices

**Recommended order** (in separate terminal windows):

1. **Start Eureka Server** (required for service discovery):
```bash
cd microservices/eureka_server/eureka_server
./gradlew bootRun
```
Access dashboard: `http://localhost:8761`

2. **Start Auth Service**:
```bash
cd microservices/auth-service/auth-service
./gradlew bootRun
```

3. **Start Hotel Service**:
```bash
cd microservices/hotel_service/hotel_service
./gradlew bootRun
```

4. **Start Room Service**:
```bash
cd microservices/room_service/room_service
./gradlew bootRun
```

5. **Start Customer Service**:
```bash
cd microservices/customer-service/customer-service
./gradlew bootRun
```

6. **Start Booking Service**:
```bash
cd microservices/booking_service/booking_service
./gradlew bootRun
```

7. **Start Notification Service** (Kafka consumer):
```bash
cd microservices/notification_service/notification_service
./gradlew bootRun
```

8. **Start API Gateway** (last, as entry point):
```bash
cd microservices/api_gateway/api_gateway
./gradlew bootRun
```

### Verification ✅

Once all services are running:

1. **Check Eureka Dashboard**: Visit `http://localhost:8761` to verify all services are registered
2. **Test API Gateway**: Send request to `http://localhost:7777/auth/login` to verify gateway routing
3. **Check Kafka Broker**: Verify Kafka topic "booking" exists and messages flow properly

---

## API Usage Examples 💻

### 1. Register a New Customer

```bash
curl -X POST http://localhost:7777/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "name": "John Doe",
    "phoneNumber": "1234567890"
  }'
```

### 2. Login and Get JWT Token

```bash
curl -X POST http://localhost:7777/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

Response includes JWT token.

### 3. Create a Hotel

```bash
curl -X POST http://localhost:7777/hotels \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "hotelName": "Grand Hotel",
    "rating": 5,
    "address": {
      "city": "New York",
      "state": "NY",
      "postalCode": "10001",
      "country": "USA"
    }
  }'
```

### 4. Create a Room

```bash
curl -X POST http://localhost:7777/rooms \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "hotelId": 1,
    "roomType": "DOUBLE",
    "pricePerNight": 200,
    "checkInTime": "02:00 PM",
    "checkOutTime": "11:00 AM",
    "amenities": ["AC", "WIFI", "TV"]
  }'
```

### 5. Create a Booking

```bash
curl -X POST http://localhost:7777/bookings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "customerId": 1,
    "roomId": 1,
    "bookedFrom": "2024-12-20",
    "bookedUntil": "2024-12-25"
  }'
```

After booking creation, a Kafka event is published and the Notification Service sends an email notification.

### 6. Get All Bookings

```bash
curl -X GET http://localhost:7777/bookings \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## Architecture Diagram 🏗️

```
┌─────────────────────────────────────────────────────────────┐
│                     Client Application                      │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
                    ┌──────────────┐
                    │  API Gateway │ (7777)
                    │  (Load Balancer)
                    └──┬───────────┘
                       │
        ┌──────────────┼──────────────┬──────────────┬──────────────┐
        │              │              │              │              │
        ▼              ▼              ▼              ▼              ▼
    ┌────────┐  ┌──────────┐  ┌────────┐  ┌──────────┐  ┌─────────┐
    │  Auth  │  │  Hotel   │  │  Room  │  │ Booking  │  │ Customer│
    │Service │  │ Service  │  │Service │  │ Service  │  │ Service │
    │(1111)  │  │ (7070)   │  │(8080)  │  │ (9090)   │  │ (6060)  │
    └──┬─────┘  └────┬─────┘  └───┬────┘  └────┬─────┘  └────┬────┘
       │             │            │             │             │
       └─────────────┴────────────┴─────────────┴─────────────┘
                     │
                     │ (Service Discovery)
                     ▼
              ┌──────────────────┐
              │ Eureka Server    │
              │ (8761)           │
              └──────────────────┘

    Booking Service  ──(Kafka)──> Notification Service (5050)
                     (Published)     (Consumer)
                     │
                     ▼
          Kafka Topic: "booking"
          (BookingDetails events)
```

---

## Configuration Files 🛠️

### Key Application Properties

Each microservice has configuration files:
- `application.properties` - Base configuration
- `application-dev.properties` - Development profile
- `application-prod.properties` - Production profile

### Database
- H2 in-memory database for each service
- Auto-initialized on startup
- Isolated schemas per microservice

### Logging
- SLF4J with Logback
- Production profile includes file logging
- Log directory: `logs/app.log`

---

## Monitoring and Observability 📈

### Metrics
- Prometheus metrics exposed at `/actuator/prometheus` on each service
- Default path for scraping: `http://localhost:<port>/actuator/prometheus`

### Distributed Tracing
- Enabled via Micrometer and OpenTelemetry
- Trace probability: 100% in dev, 10% in prod
- Supports correlation of requests across services

### Metrics Collected
- HTTP server requests
- JVM metrics
- Application-specific metrics
- Booking and authentication events

---

## Troubleshooting 🐞

### Issue: Services not registering with Eureka
**Solution**: Ensure Eureka Server is started first and running on port 8761

### Issue: Kafka messages not consumed
**Solution**: 
1. Verify Kafka broker is running
2. Check topic "booking" exists: `kafka-topics --list --bootstrap-server localhost:9092`
3. Check Notification Service logs for consumer group

### Issue: Payload library not found
**Solution**: Run `mvn clean install` in `hotel-booking-microservice-payload/payload` directory

### Issue: JWT validation fails
**Solution**: Ensure token is sent in Authorization header: `Authorization: Bearer <token>`

---

## Performance Features ⚡

- **Virtual Threads**: Enabled for improved concurrency and resource efficiency
- **Load Balancing**: Client-side load balancing via Eureka and Spring Cloud LoadBalancer
- **Feign Clients**: Declarative HTTP client for inter-service communication
- **H2 Database**: In-memory database for fast development and testing

---

## Future Enhancements 🚧

- Database migration to persistent databases (PostgreSQL/MySQL)
- API documentation with Swagger/OpenAPI
- Container orchestration with Kubernetes
- CI/CD pipeline integration
- Enhanced security with OAuth2/OIDC
- Rate limiting and request throttling
- Caching layer (Redis)
- Saga pattern for distributed transactions

---

## Contact & Support ✉️

For issues, questions, or contributions, please contact the development team or raise an issue in the repository.

