# Smart Campus REST API

This project is a RESTful API built using JAX-RS (Jersey) for managing rooms and sensors in a smart campus system.

## Features

* Room management (create, get, update, delete)
* Sensor management (linked to rooms)
* Prevent deleting rooms with sensors
* JSON request/response handling

## Technologies Used

* Java
* JAX-RS (Jersey)
* Maven
* NetBeans

## API Endpoints

### Rooms

GET all rooms:
GET /api/v1/rooms

GET room by ID:
GET /api/v1/rooms/{id}

CREATE room:
POST /api/v1/rooms

UPDATE room:
PUT /api/v1/rooms/{id}

DELETE room:
DELETE /api/v1/rooms/{id}

---

### Sensors

CREATE sensor:
POST /api/v1/sensors

DELETE sensor:
DELETE /api/v1/sensors/{id}

---

## Example (Postman)

Create Room:
{
"id": "R1",
"name": "Library",
"capacity": 50
}

Create Sensor:
{
"id": "S1",
"type": "temperature",
"roomId": "R1"
}

---

## Notes

* Rooms cannot be deleted if sensors are attached
* Sensors must belong to an existing room
