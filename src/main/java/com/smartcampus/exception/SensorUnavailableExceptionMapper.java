package com.smartcampus.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

// maps SensorUnavailableException to HTTP 403 response
// used when a sensor cannot be used (e.g. under maintenance)
@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException ex) {

        // return 403 forbidden with error message
        return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}