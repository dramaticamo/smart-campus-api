package com.smartcampus.resource;

import com.smartcampus.model.Room;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;

@Path("/rooms")
public class RoomResource {

    // in-memory storage
    public static Map<String, Room> rooms = new HashMap<>();

    // ✅ GET all rooms
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Room> getAllRooms() {
        return rooms.values();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Room getRoomById(@PathParam("id") String id) {
        System.out.println("Requested ID: " + id); // debug
        return rooms.get(id);
    }

    // ✅ POST create room
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Room createRoom(Room room) {
        rooms.put(room.getId(), room);
        return room;
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoom(@PathParam("id") String id, Room updatedRoom) {

        Room existingRoom = rooms.get(id);

        if (existingRoom == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Room not found")
                    .build();
        }

        existingRoom.setName(updatedRoom.getName());
        existingRoom.setCapacity(updatedRoom.getCapacity());

        return Response.ok(existingRoom).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("id") String id) {

        Room room = rooms.get(id);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Room not found"))
                    .build();
        }

        if (!room.getSensorIds().isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Room does not exist"))
                    .build();
        }

        rooms.remove(id);

        return Response.ok(Map.of("message", "Room deleted")).build();
    }
}