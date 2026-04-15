package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.model.Room;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.smartcampus.exception.LinkedResourceNotFoundException;

import java.util.*;

@Path("/sensors")
// handles all sensor-related endpoints
public class SensorResource {

    // store sensors in memory
    private static Map<String, Sensor> sensors = new HashMap<>();

    // allow other classes (like sub-resource) to access sensors
    public static Map<String, Sensor> getSensorsMap() {
        return sensors;
    }

    // get all sensors (with optional filtering by type)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Sensor> getSensors(@QueryParam("type") String type) {

        // if no filter, return all sensors
        if (type == null) {
            return sensors.values();
        }

        // filter sensors by type
        List<Sensor> filtered = new ArrayList<>();

        for (Sensor sensor : sensors.values()) {
            if (sensor.getType().equalsIgnoreCase(type)) {
                filtered.add(sensor);
            }
        }

        return filtered;
    }

    // create a new sensor
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {

        // check if room exists before linking sensor
        Room room = RoomResource.rooms.get(sensor.getRoomId());

        if (room == null) {
            throw new LinkedResourceNotFoundException("Room does not exist");
        }

        // store sensor
        sensors.put(sensor.getId(), sensor);

        // link sensor to room
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    // sub-resource locator for sensor readings
    @Path("/{id}/readings")
    public SensorReadingResource getReadingResource(@PathParam("id") String id) {
        return new SensorReadingResource(id);
    }

    // delete a sensor
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSensor(@PathParam("id") String id) {

        Sensor sensor = sensors.get(id);

        // return 404 if sensor not found
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Sensor not found"))
                    .build();
        }

        // remove sensor from its room
        Room room = RoomResource.rooms.get(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().remove(id);
        }

        // remove sensor from storage
        sensors.remove(id);

        return Response.ok("Sensor deleted").build();
    }
}