package com.mycompany.smart.campus.api;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class SmartCampusApi {

    public static final String BASE_URI = "http://localhost:8080/";

    public static HttpServer startServer() {
    final ResourceConfig rc = new ResourceConfig()
    .register(com.smartcampus.resource.DiscoveryResource.class)
    .register(com.smartcampus.resource.RoomResource.class)
    .register(com.smartcampus.resource.SensorResource.class);

    return GrizzlyHttpServerFactory.createHttpServer(
            URI.create(BASE_URI + "api/v1/"), rc);
}

    public static void main(String[] args) {
        final HttpServer server = startServer();
        System.out.println("Server started at " + BASE_URI + "api/v1");
    }
}