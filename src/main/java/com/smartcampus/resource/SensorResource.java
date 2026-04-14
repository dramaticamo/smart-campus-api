package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.model.Room;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/sensors")
public class SensorResource {

    private static Map<String, Sensor> sensors = new HashMap<>();

    // GET all sensors
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Sensor> getAllSensors() {
        return sensors.values();
    }

    // POST create sensor
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {

        // 🔥 IMPORTANT RULE: room must exist
        Room room = RoomResource.rooms.get(sensor.getRoomId());

        if (room == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Room does not exist")
                    .build();
        }

        sensors.put(sensor.getId(), sensor);

        // 🔥 Link sensor to room
        room.getSensorIds().add(sensor.getId());

        return Response.ok(sensor).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSensor(@PathParam("id") String id) {

        Sensor sensor = sensors.get(id);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found")
                    .build();
        }

        // 🔥 remove sensor from room
        Room room = RoomResource.rooms.get(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().remove(id);
        }

        sensors.remove(id);

        return Response.ok("Sensor deleted").build();
    }
}