# Smart Campus Sensor & Room Management API

## Overview

This project implements a RESTful API for the university's **"Smart Campus"** initiative using **JAX-RS (Jersey)** with an embedded **Grizzly HTTP server**. The API manages Rooms, Sensors, and Sensor Readings across campus, providing a seamless interface for campus facilities managers and automated building systems to interact with campus data.

The API follows RESTful architectural principles with proper resource nesting, comprehensive error handling via custom Exception Mappers, and full request/response logging through JAX-RS filters.

### Technology Stack

- **Java 17**
- **JAX-RS 3.1 (Jersey)** — Jakarta RESTful Web Services implementation
- **Grizzly HTTP Server** — Lightweight embedded HTTP server
- **Jackson** — JSON serialization/deserialization
- **Maven** — Build and dependency management
- **In-memory data structures** — `ConcurrentHashMap` for thread-safe storage

### API Architecture

```
/api/v1/                          → Discovery endpoint (API metadata & navigation)
/api/v1/rooms                     → Room collection resource
/api/v1/rooms/{roomId}            → Individual room resource
/api/v1/sensors                   → Sensor collection resource
/api/v1/sensors/{sensorId}        → Individual sensor resource
/api/v1/sensors/{sensorId}/readings → Sensor reading sub-resource (historical data)
```

### Core Data Models

1. **Room** — Represents a physical room on campus (`id`, `name`, `capacity`, `sensorIds`)
2. **Sensor** — Represents a sensor device (`id`, `type`, `status`, `currentValue`, `roomId`)
3. **SensorReading** — Represents a historical data point (`id`, `timestamp`, `value`)

---

## How to Build and Run

### Prerequisites

- **Java 17** (or later)
- **Apache Maven 3.6+**

### Step-by-Step Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/dramaticamo/smart-campus-api.git
   cd smart-campus-api
   ```

2. **Build the project:**
   ```bash
   mvn clean package
   ```

3. **Run the server:**
   ```bash
   java -jar target/smart-campus-api-1.0-SNAPSHOT.jar
   ```

4. The server will start at: **http://localhost:8080/api/v1/**

5. **Verify it is running:**
   ```bash
   curl http://localhost:8080/api/v1/
   ```

---

## Sample curl Commands

### 1. Discovery Endpoint — Get API metadata
```bash
curl -X GET http://localhost:8080/api/v1/
```

### 2. Create a Room
```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id": "LIB-301", "name": "Library Quiet Study", "capacity": 50}'
```

### 3. Get All Rooms
```bash
curl -X GET http://localhost:8080/api/v1/rooms
```

### 4. Get a Specific Room by ID
```bash
curl -X GET http://localhost:8080/api/v1/rooms/LIB-301
```

### 5. Create a Sensor (linked to an existing room)
```bash
curl -X POST http://localhost:8080/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id": "TEMP-001", "type": "Temperature", "status": "ACTIVE", "currentValue": 0.0, "roomId": "LIB-301"}'
```

### 6. Get All Sensors (with optional type filter)
```bash
curl -X GET http://localhost:8080/api/v1/sensors
curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"
```

### 7. Post a Sensor Reading (sub-resource)
```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value": 22.5}'
```

### 8. Get Sensor Reading History
```bash
curl -X GET http://localhost:8080/api/v1/sensors/TEMP-001/readings
```

### 9. Delete a Room (will fail if sensors are still attached — returns 409)
```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301
```

### 10. Delete a Sensor, then Delete the Room
```bash
curl -X DELETE http://localhost:8080/api/v1/sensors/TEMP-001
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301
```

---

## Report — Answers to Coursework Questions

### Part 1: Service Architecture & Setup

**Q1: Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton?**

By default, JAX-RS Resource classes follow a **per-request lifecycle**. This means the JAX-RS runtime (Jersey, in our case) creates a **new instance** of the resource class for every incoming HTTP request. Once the request is processed and the response is sent, the instance is discarded and eligible for garbage collection.

This architectural decision has significant implications for how we manage in-memory data structures. Since each request receives a fresh instance, any **instance-level (non-static) fields** would be reset on every request, leading to data loss. To persist data across requests using in-memory storage, we must use **static fields** (e.g., `public static Map<String, Room> rooms = new ConcurrentHashMap<>()`). However, this introduces the challenge of **thread safety** — multiple concurrent requests may try to read from and write to the shared static data simultaneously. To prevent race conditions and data corruption, we use **`ConcurrentHashMap`** instead of `HashMap`, which provides thread-safe operations without requiring explicit synchronization blocks, ensuring data integrity across concurrent API calls.

**Q2: Why is the provision of "Hypermedia" (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?**

HATEOAS (Hypermedia As The Engine Of Application State) is considered the highest maturity level of REST (Level 3 in the Richardson Maturity Model). It means that API responses include **hyperlinks** that tell the client what actions are available and where to navigate next, rather than requiring the client to hard-code URL paths.

The benefits for client developers are:
- **Discoverability**: Clients can explore the API dynamically by following links, reducing their dependency on external documentation.
- **Loose coupling**: If the server changes its URL structure, clients that follow links (rather than constructing URLs themselves) will automatically adapt without code changes.
- **Self-documentation**: The API response itself serves as a guide, reducing the learning curve for new developers.
- **Reduced errors**: Clients do not need to guess or construct URLs manually, which minimizes mistakes from incorrect path construction.

In our implementation, the Discovery endpoint at `GET /api/v1` provides a `_links` object and a `resources` map that guide clients to the available resource collections.

---

### Part 2: Room Management

**Q: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.**

Returning **only IDs** has the advantage of **smaller payload sizes**, which reduces network bandwidth consumption. This is especially beneficial when there are thousands of rooms. However, it requires the client to make **additional HTTP requests** (one per room) to fetch the full details, which increases latency and the total number of network round trips. This is known as the **N+1 problem**.

Returning **full room objects** increases the initial payload size and consumes more bandwidth. However, it provides a **complete dataset in a single request**, eliminating the need for follow-up calls. This reduces client-side complexity and overall latency, as the client can immediately render or process the data without additional API calls.

In practice, the preferred approach depends on the use case. For our Smart Campus API, we return **full room objects** because the dataset is relatively small (campus rooms) and the client typically needs all room details to display a management dashboard. For very large collections, pagination (e.g., `?page=1&limit=20`) would be the recommended solution to balance both concerns.

**Q: Is the DELETE operation idempotent in your implementation? Provide a detailed justification.**

Yes, the DELETE operation is **idempotent** in terms of the final state of the server. Idempotency means that making the same request multiple times produces the same result as making it once.

- **First DELETE request**: The room exists and is successfully deleted. The server returns `200 OK` with a success message.
- **Second (and subsequent) DELETE requests for the same room ID**: The room no longer exists in the system. The server returns `404 Not Found` with an error message stating the room was not found.

While the **HTTP status codes differ** between the first and subsequent calls (200 vs. 404), the **server-side state remains the same** after the first deletion — the room is gone and stays gone. This satisfies the definition of idempotency: the server state after N identical requests is the same as after 1 request. The different response codes simply inform the client about what happened, but they do not change the server's state further.

---

### Part 3: Sensor Operations & Linking

**Q: We explicitly use the `@Consumes(MediaType.APPLICATION_JSON)` annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as `text/plain` or `application/xml`.**

When a method is annotated with `@Consumes(MediaType.APPLICATION_JSON)`, JAX-RS enforces strict **content negotiation**. If a client sends a request with a `Content-Type` header that does not match `application/json` (e.g., `text/plain` or `application/xml`), the JAX-RS runtime will automatically reject the request **before it even reaches the method body**. The server will return an **HTTP 415 Unsupported Media Type** response.

This is a built-in JAX-RS mechanism that acts as a gatekeeper — it ensures that only properly formatted JSON data is passed to the Jackson deserializer, preventing potential parsing errors, data corruption, or unexpected behavior that could arise from attempting to deserialize non-JSON content as a Java object.

**Q: You implemented filtering using `@QueryParam`. Contrast this with an alternative design where the type is part of the URL path (e.g., `/api/v1/sensors/type/CO2`). Why is the query parameter approach generally considered superior for filtering and searching collections?**

The query parameter approach (`/api/v1/sensors?type=CO2`) is generally considered superior for several reasons:

1. **Semantics**: In REST, the URL path identifies a **resource** or a **resource hierarchy**. `/api/v1/sensors/type/CO2` implies that `type` and `CO2` are sub-resources of sensors, which is semantically incorrect — they are not resources, they are filters.

2. **Optional parameters**: Query parameters are inherently optional. A client can call `/api/v1/sensors` to get all sensors, or add `?type=CO2` to filter. With a path-based approach, you would need separate endpoints for filtered and unfiltered access.

3. **Composability**: Multiple query parameters can be easily combined (e.g., `?type=CO2&status=ACTIVE`). With path segments, combining multiple filters becomes cumbersome and creates a URL explosion: `/api/v1/sensors/type/CO2/status/ACTIVE`.

4. **Caching**: Caches and proxies understand that query parameters represent variations of the same resource, whereas different path segments are treated as entirely different resources.

5. **Convention**: Query parameters for filtering are a widely accepted REST convention, making the API more intuitive for developers who are familiar with standard REST patterns.

---

### Part 4: Deep Nesting with Sub-Resources

**Q: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs?**

The Sub-Resource Locator pattern provides several architectural benefits:

1. **Separation of Concerns**: Each resource class is responsible for a single, well-defined entity. `SensorResource` handles sensor CRUD operations, while `SensorReadingResource` handles reading history. This follows the **Single Responsibility Principle**, making each class focused, readable, and maintainable.

2. **Reduced Class Size**: Without sub-resource locators, all endpoints for sensors AND their readings would be crammed into a single `SensorResource` class. In a large API with many nested resources, this "mega-controller" approach leads to classes with hundreds or thousands of lines, making them difficult to navigate, test, and maintain.

3. **Reusability**: The `SensorReadingResource` class can potentially be reused in different contexts if needed, since it is decoupled from the parent resource's implementation details. It only needs the `sensorId` to operate.

4. **Contextual Delegation**: The locator method in `SensorResource` validates that the parent sensor exists before delegating to the sub-resource. This ensures that business rules (e.g., "the sensor must exist before accessing its readings") are enforced at the appropriate level.

5. **Scalability of Codebase**: As the API grows (e.g., adding maintenance logs, calibration history, alerts per sensor), new sub-resource classes can be added independently without modifying the parent class significantly. This modular architecture scales much better than a monolithic controller.

---

### Part 5: Advanced Error Handling, Exception Mapping & Logging

**Q: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?**

HTTP 404 means "the resource you requested was not found." It refers to the **target URL** of the request — the resource the client is trying to access or create. HTTP 422 (Unprocessable Entity) means "the server understood the request, and the syntax of the payload is valid, but the server is unable to process the contained instructions."

When a client sends a `POST /api/v1/sensors` with a valid JSON body containing a `roomId` that does not exist, the issue is not with the **target resource** (sensors) — that collection exists and is accessible. The issue is with the **content** of the request body: it references a linked resource (a room) that cannot be found. The JSON is syntactically valid, but **semantically invalid** because it references a non-existent dependency.

Using 404 would be misleading because it would suggest that the `/api/v1/sensors` endpoint itself does not exist. HTTP 422 accurately communicates that the request was well-formed but could not be processed due to a **validation failure** within the payload — specifically, a broken reference to another resource. This distinction helps client developers quickly identify that the problem lies in their data (the `roomId` value), not in the URL they are calling.

**Q: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?**

Exposing internal Java stack traces to external API consumers is a serious security vulnerability known as **information leakage**. An attacker could gather the following specific information:

1. **Technology stack**: The stack trace reveals the programming language (Java), the framework (Jersey/JAX-RS), the server (Grizzly), and their versions. This allows an attacker to search for **known vulnerabilities (CVEs)** specific to those versions.

2. **Internal package structure**: Class names and package paths (e.g., `com.smartcampus.resource.SensorResource.createSensor()`) reveal the internal architecture, class hierarchy, and naming conventions of the application.

3. **File paths and line numbers**: The trace may reveal the server's file system structure and exact locations of source files, which can help an attacker understand the deployment environment.

4. **Database details**: If the error involves a database operation, the trace might expose database driver names, connection strings, table names, or SQL queries.

5. **Third-party libraries**: Dependencies listed in the trace reveal the full software supply chain, each of which could have exploitable vulnerabilities.

6. **Business logic insights**: The method names and call chain can reveal how the application processes requests, potentially exposing logic flaws that can be exploited.

Our implementation uses a **catch-all `ExceptionMapper<Throwable>`** (the "Global Safety Net") that intercepts all unhandled exceptions and returns a **generic HTTP 500** response with no internal details. The actual exception details are logged server-side using `java.util.logging.Logger` for debugging purposes, but never exposed to the client.

**Q: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting `Logger.info()` statements inside every single resource method?**

Using JAX-RS filters for cross-cutting concerns like logging offers several advantages:

1. **DRY (Don't Repeat Yourself)**: A single filter class handles logging for **every** request and response across the entire API. Without filters, you would need to manually add `Logger.info()` statements at the beginning and end of every single resource method, leading to massive code duplication.

2. **Separation of Concerns**: Logging is an infrastructure concern, not a business logic concern. Mixing logging code with business logic makes resource methods harder to read and maintain. Filters keep these concerns cleanly separated.

3. **Consistency**: A filter guarantees that **every** request and response is logged uniformly, regardless of which developer wrote the resource method. With manual logging, it is easy to forget to add it to a new method, or to use inconsistent formats.

4. **Maintainability**: If the logging format or level needs to change (e.g., adding a request ID, switching to structured JSON logging), you only need to modify the **single filter class** instead of updating dozens of resource methods across the codebase.

5. **No modification to existing code**: Filters can be added to or removed from the application without touching any resource class. They are registered through the `ResourceConfig` and operate transparently at the container level, following the **Open/Closed Principle** — the application is open for extension but closed for modification.

---

## Project Structure

```
smart-campus-api/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    └── main/
        └── java/
            ├── com/mycompany/smart/campus/api/
            │   └── SmartCampusApi.java              # Main class & Grizzly server bootstrap
            └── com/smartcampus/
                ├── config/
                │   └── AppConfig.java               # @ApplicationPath configuration
                ├── model/
                │   ├── Room.java                     # Room POJO
                │   ├── Sensor.java                   # Sensor POJO
                │   └── SensorReading.java            # SensorReading POJO
                ├── resource/
                │   ├── DiscoveryResource.java        # GET /api/v1 — API discovery
                │   ├── RoomResource.java             # /api/v1/rooms — Room CRUD
                │   ├── SensorResource.java           # /api/v1/sensors — Sensor CRUD + sub-resource locator
                │   └── SensorReadingResource.java    # /api/v1/sensors/{id}/readings — Reading sub-resource
                ├── exception/
                │   ├── RoomNotEmptyException.java              # Custom: room has sensors
                │   ├── RoomNotEmptyExceptionMapper.java        # Maps to 409 Conflict
                │   ├── LinkedResourceNotFoundException.java    # Custom: referenced room missing
                │   ├── LinkedResourceNotFoundExceptionMapper.java # Maps to 422 Unprocessable Entity
                │   ├── SensorUnavailableException.java         # Custom: sensor in MAINTENANCE
                │   ├── SensorUnavailableExceptionMapper.java   # Maps to 403 Forbidden
                │   └── GenericExceptionMapper.java             # Catch-all: maps to 500
                └── filter/
                    └── LoggingFilter.java             # Request & Response logging filter
```

---

## Error Handling Summary

| Scenario | Exception | HTTP Status | Code |
|---|---|---|---|
| Delete room with active sensors | `RoomNotEmptyException` | 409 Conflict | Custom mapper |
| Create sensor with non-existent room | `LinkedResourceNotFoundException` | 422 Unprocessable Entity | Custom mapper |
| Post reading to MAINTENANCE sensor | `SensorUnavailableException` | 403 Forbidden | Custom mapper |
| Any unhandled runtime exception | `Throwable` (catch-all) | 500 Internal Server Error | Generic mapper |
