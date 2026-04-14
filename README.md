# Smart Campus REST API

## Overview

This project is a REST API built using JAX-RS (Jersey). It is designed to manage rooms, sensors, and sensor readings in a smart campus system. The system uses in-memory storage (HashMap and ArrayList) instead of a database.

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

```json id="ex1"
{
  "id": "R1",
  "name": "Library",
  "capacity": 100
}
```

### Create Sensor

```json id="ex2"
{
  "id": "S1",
  "type": "Temperature",
  "status": "ACTIVE",
  "roomId": "R1"
}
```

### Add Reading

```json id="ex3"
{
  "value": 25.5,
  "timestamp": 1710000000
}
```

---

## How to Run

1. Clone the project:

```
git clone https://github.com/dramaticamo/smart-campus-api.git
```

2. Open the project folder:

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

In JAX-RS, a new object is created for each request. This helps avoid problems when many users use the API at the same time. In this project, static HashMaps are used to store data so it is shared between requests.

---

### HATEOAS

HATEOAS means the API can give links to other endpoints in the response. This helps users understand how to use the API without reading documentation.

---

### QueryParam vs PathParam

Query parameters are used for filtering, like:

```
/sensors?type=CO2
```

This is better than putting it in the path because it is more flexible.

---

### DELETE Idempotency

DELETE is idempotent, which means if you send the same DELETE request many times, the result will be the same. The resource will be deleted once, and nothing changes after that.

---

### HTTP 422 vs 404

HTTP 422 is used when the request is correct but the data inside it is wrong. For example, creating a sensor with a room that does not exist. This is better than 404 because the endpoint exists, but the data is invalid.

---

### Security

If we return full error messages, users can see internal system details. This can be dangerous. So this project uses a global exception handler to return simple error messages.

---

### Logging

Logging is done using JAX-RS filters. This records every request and response. It is better than writing logging code in every method.

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

A video demonstration using Postman is included with this project.
