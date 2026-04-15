package com.smartcampus.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

// maps LinkedResourceNotFoundException to HTTP 422 response
// used when request is valid but contains invalid data (e.g. wrong roomId)
@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {

        // return 422 with error message
        return Response.status(422)
                .entity(Map.of("error", ex.getMessage()))
                .build();
    }
}