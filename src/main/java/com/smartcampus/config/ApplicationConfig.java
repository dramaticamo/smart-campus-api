package com.smartcampus.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

// defines the base path for all REST endpoints
// all APIs will be accessed using /api/v1/
@ApplicationPath("/api/v1")
public class ApplicationConfig extends Application {
    // no extra configuration needed here
}