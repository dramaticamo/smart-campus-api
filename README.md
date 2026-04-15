# Smart Campus REST API

## Overview

This project is a REST API built using JAX-RS (Jersey). It manages rooms, sensors, and sensor readings in a smart campus system. The system uses in-memory storage (HashMap and ArrayList) instead of a database.

---

## Technologies Used

* Java
* JAX-RS (Jersey)
* Grizzly Server
* Maven

---

## Features

### Room Management

* Create a room
* Get all rooms
* Get a room by ID
* Update a room
* Delete a room (only if it has no sensors)

Each room has:

* id
* name
* capacity
* sensorIds

---

### Sensor Management

* Create sensors
* Get all sensors
* Filter sensors by type

Each sensor has:

* id
* type
* status (ACTIVE, MAINTENANCE, OFFLINE)
* currentValue
* roomId

---

### Sensor Readings

* Add readings to a sensor
* Get all readings for a sensor

Each reading has:

* id
* value
* timestamp

---

## API Endpoints

### Rooms

* GET /api/v1/rooms
* POST /api/v1/rooms
* GET /api/v1/rooms/{id}
* PUT /api/v1/rooms/{id}
* DELETE /api/v1/rooms/{id}

---

### Sensors

* GET /api/v1/sensors
* GET /api/v1/sensors?type=Temperature
* POST /api/v1/sensors

---

### Sensor Readings

* GET /api/v1/sensors/{id}/readings
* POST /api/v1/sensors/{id}/readings

---

## Example JSON

### Create Room

```json
{
  "id": "R1",
  "name": "Library",
  "capacity": 100
}
```

### Create Sensor

```json
{
  "id": "S1",
  "type": "Temperature",
  "status": "ACTIVE",
  "roomId": "R1"
}
```

### Add Reading

```json
{
  "id": "READ1",
  "value": 25.5,
  "timestamp": 1710000000
}
```

---

## Example curl Commands

```bash
# Create room
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"R1","name":"Library","capacity":100}'

# Get all rooms
curl http://localhost:8080/api/v1/rooms

# Create sensor
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"S1","type":"Temperature","status":"ACTIVE","roomId":"R1"}'

# Get sensors with filter
curl http://localhost:8080/api/v1/sensors?type=Temperature

# Add reading
curl -X POST http://localhost:8080/api/v1/sensors/S1/readings \
-H "Content-Type: application/json" \
-d '{"id":"READ1","value":25.5,"timestamp":1710000000}'
```

---

## How to Run

1. Clone the project:

```
git clone https://github.com/dramaticamo/smart-campus-api.git
```

2. Open the folder:

```
cd smart-campus-api
```

3. Run the project:

```
mvn compile exec:java
```

4. Open in browser or Postman:

```
http://localhost:8080/api/v1/
```

---

## Design Explanation

### JAX-RS Resource Lifecycle

In JAX-RS, a new object is created for each request. This helps avoid issues when multiple users access the API at the same time. In this project, static HashMaps are used so the data is shared across requests.

---

### HATEOAS

HATEOAS means the API can return links to other endpoints. This helps users navigate the API without needing extra documentation.

---

### QueryParam vs PathParam

Query parameters are used for filtering, for example:

```
/sensors?type=CO2
```

This is better than using path parameters because it allows more flexible searching.

---

### DELETE Idempotency

DELETE is idempotent, which means sending the same DELETE request multiple times gives the same result. The resource is deleted once and does not change after that.

---

### HTTP 422 vs 404

HTTP 422 is used when the request is correct but the data inside is invalid. For example, creating a sensor with a room that does not exist. 404 is used when the resource itself is not found.

---

### Media Type Mismatch

If a client sends data in a different format like text or XML instead of JSON, the API will reject it. JAX-RS returns a 415 Unsupported Media Type error. This ensures only valid formats are accepted.

---

### Sub-resource Pattern

The sub-resource locator pattern helps organise the API into smaller parts. It separates logic into different classes, making the code easier to manage and understand.

---

### Security

If detailed error messages or stack traces are returned, users may see internal system information such as class names, file paths, and server details. This can be a security risk because attackers can use this information to understand how the system works. 

To prevent this, the API uses a global exception handler to return simple error messages instead of exposing internal details.

---

### Logging

Logging is implemented using JAX-RS filters. This allows requests and responses to be logged in one place instead of repeating code in every method.

---

## Error Handling

Custom exceptions are used:

* RoomNotEmptyException → 409
* LinkedResourceNotFoundException → 422
* SensorUnavailableException → 403
* GlobalException → 500

---

## Notes

* No database is used
* Data is stored in memory
* The API follows REST principles

---

## Video

A Postman video demonstration is included with this project.
