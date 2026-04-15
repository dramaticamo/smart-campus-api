package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.smartcampus.exception.SensorUnavailableException;

import java.util.List;

// handles sensor readings as a sub-resource of Sensor
public class SensorReadingResource {

    private String sensorId; // ID of the sensor this resource belongs to

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // get all readings for a specific sensor
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings() {

        Sensor sensor = SensorResource.getSensorsMap().get(sensorId);

        // return 404 if sensor not found
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found")
                    .build();
        }

        return Response.ok(sensor.getReadings()).build();
    }

    // add a new reading to a sensor
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {

        Sensor sensor = SensorResource.getSensorsMap().get(sensorId);

        // return 404 if sensor not found
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found")
                    .build();
        }

        // check if sensor is under maintenance
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is under maintenance");
        }

        // add reading to list
        sensor.getReadings().add(reading);

        // update current value with latest reading
        sensor.setCurrentValue(reading.getValue());

        return Response.ok(sensor).build();
    }
}