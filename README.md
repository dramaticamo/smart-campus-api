# Smart Campus REST API

This project is a RESTful API built using JAX-RS (Jersey) for managing rooms and sensors in a smart campus system.

## Features

### Room Management
- Create, retrieve, update, and delete rooms
- Each room has:
  - id
  - name
  - capacity
  - sensorIds

### Sensor Management
- Create and retrieve sensors
- Filter sensors by roomId
- Each sensor has:
  - id
  - type
  - roomId

### Sensor Readings (Sub-resource)
- Add readings to a sensor
- Retrieve all readings for a sensor
- Each reading contains:
  - value
  - timestamp

## API Endpoints

### Rooms
- GET /rooms
- POST /rooms
- GET /rooms/{id}
- PUT /rooms/{id}
- DELETE /rooms/{id}

### Sensors
- GET /sensors
- GET /sensors?roomId=R1
- POST /sensors

### Sensor Readings
- POST /sensors/{id}/readings
- GET /sensors/{id}/readings

## Example JSON

### Create Room
{
  "id": "R1",
  "name": "Library",
  "capacity": 100
}

### Create Sensor
{
  "id": "S1",
  "type": "temperature",
  "roomId": "R1"
}

### Add Reading
{
  "value": 25.5,
  "timestamp": 1710000000
}

## Technologies
- Java
- JAX-RS (Jersey)
- Maven
