package com.smartcampus.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.logging.Logger;

// this filter logs all incoming requests and outgoing responses
// helps with debugging and monitoring the API
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    // logger used to print messages to console
    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // log request method and URL (e.g. GET /rooms)
        LOGGER.info("Request: " + requestContext.getMethod() + " " + requestContext.getUriInfo().getRequestUri());
    }

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        // log response status (e.g. 200, 404, 500)
        LOGGER.info("Response: " + requestContext.getMethod() + " -> Status: " + responseContext.getStatus());
    }
}