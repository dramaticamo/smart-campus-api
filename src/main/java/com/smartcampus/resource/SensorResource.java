package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.model.Room;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.smartcampus.exception.LinkedResourceNotFoundException;

import java.util.*;

@Path("/sensors")
public class SensorResource {

    private static Map<String, Sensor> sensors = new HashMap<>();
    
    public static Map<String, Sensor> getSensorsMap() {
    return sensors;
}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Sensor> getSensors(@QueryParam("type") String type) {

        if (type == null) {
            return sensors.values();
        }

        List<Sensor> filtered = new ArrayList<>();

        for (Sensor sensor : sensors.values()) {
            if (sensor.getType().equalsIgnoreCase(type)) {
                filtered.add(sensor);
            }
        }

        return filtered;
    }

    // POST create sensor
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {

        Room room = RoomResource.rooms.get(sensor.getRoomId());

        if (room == null) {
            throw new LinkedResourceNotFoundException("Room does not exist");
        }

        sensors.put(sensor.getId(), sensor);

        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED)
        .entity(sensor)
        .build();
    }
    
    @Path("/{id}/readings")
    public SensorReadingResource getReadingResource(@PathParam("id") String id) {
        return new SensorReadingResource(id);
    }
    
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSensor(@PathParam("id") String id) {

        Sensor sensor = sensors.get(id);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Sensor not found"))
                    .build();
        }

        Room room = RoomResource.rooms.get(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().remove(id);
        }

        sensors.remove(id);

        return Response.ok("Sensor deleted").build();
    }
}