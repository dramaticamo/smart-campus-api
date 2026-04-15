package com.smartcampus.resource;

import com.smartcampus.model.Room;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.smartcampus.exception.RoomNotEmptyException;

import java.util.*;

@Path("/rooms")
// handles all room-related endpoints
public class RoomResource {

    // store rooms in memory (no database used)
    public static Map<String, Room> rooms = new HashMap<>();

    // get all rooms
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    // get a specific room by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("id") String id) {

        Room room = rooms.get(id);

        // return 404 if room not found
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Room not found"))
                    .build();
        }

        return Response.ok(room).build();
    }

    // create a new room
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {

        // store room in map
        rooms.put(room.getId(), room);

        // return 201 created
        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    // update existing room details
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoom(@PathParam("id") String id, Room updatedRoom) {

        Room existingRoom = rooms.get(id);

        // return 404 if room not found
        if (existingRoom == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Room not found")
                    .build();
        }

        // update fields
        existingRoom.setName(updatedRoom.getName());
        existingRoom.setCapacity(updatedRoom.getCapacity());

        return Response.ok(existingRoom).build();
    }

    // delete a room
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("id") String id) {

        Room room = rooms.get(id);

        // return 404 if room not found
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Room not found"))
                    .build();
        }

        // prevent deleting room if it still has sensors
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Cannot delete room with sensors");
        }

        // remove room from storage
        rooms.remove(id);

        return Response.ok(Map.of("message", "Room deleted")).build();
    }
}