package com.mycompany.smart.campus.api;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class SmartCampusApi {

    // base URL for the server
    public static final String BASE_URI = "http://localhost:8080/";

    // start the HTTP server and configure all resources
    public static HttpServer startServer() {

        // configure JAX-RS and register all API resources and exception handlers
        final ResourceConfig rc = new ResourceConfig()
            .register(com.smartcampus.resource.DiscoveryResource.class) // root endpoint
            .register(com.smartcampus.resource.RoomResource.class)      // room APIs
            .register(com.smartcampus.resource.SensorResource.class)    // sensor APIs

            // register custom exception mappers for error handling
            .register(com.smartcampus.exception.RoomNotEmptyExceptionMapper.class)
            .register(com.smartcampus.exception.LinkedResourceNotFoundExceptionMapper.class)
            .register(com.smartcampus.exception.SensorUnavailableExceptionMapper.class)
            .register(com.smartcampus.exception.GlobalExceptionMapper.class);

        // create and start the server at /api/v1/
        return GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI + "api/v1/"), rc);
    }

    public static void main(String[] args) {

        // start server
        final HttpServer server = startServer();

        // print simple messages so user knows server is running
        System.out.println("Server started at http://localhost:8080/");
        System.out.println("API available at http://localhost:8080/api/v1/");
    }
}