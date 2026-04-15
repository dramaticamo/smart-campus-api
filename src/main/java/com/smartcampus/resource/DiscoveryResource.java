package com.smartcampus.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.Map;

// root endpoint that gives basic information about the API
@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getInfo() {

        // create response map
        Map<String, Object> response = new HashMap<>();

        // basic API information
        response.put("version", "1.0");
        response.put("name", "Smart Campus API");
        response.put("contact", "admin@smartcampus.com");

        // list of available endpoints
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("rooms", "/api/v1/rooms");
        endpoints.put("sensors", "/api/v1/sensors");

        response.put("endpoints", endpoints);

        // return response as JSON
        return response;
    }
}