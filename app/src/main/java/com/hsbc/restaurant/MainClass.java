package com.hsbc.restaurant;

import io.muserver.*;
import java.sql.SQLException;
import com.hsbc.restaurant.controller.BookingHandler;

public class MainClass {

    public static void main(String[] args) {

        BookingHandler bookingHandler = new BookingHandler();

        // Specify port 8080
        MuServer server = MuServerBuilder.httpServer()
                .withHttpPort(8080)
                .addHandler(Method.POST, "/book", (request, response, pathParams) -> {
                    bookingHandler.createBooking(request, response);
                })   // Create booking
                .addHandler(Method.GET, "/bookings/{date}", (request, response, pathParams) -> {
                    String dateString = pathParams.get("date");
                    bookingHandler.getAllBookings(request, response, dateString);
                })  // Get all bookings
                .addHandler(Method.GET, "/book/{id}", (request, response, pathParams) -> {
                    String bookingId = pathParams.get("id");
                    bookingHandler.getBookingById(request, response, bookingId);
                })  // Get a booking by ID
                .addHandler(Method.PUT, "/book/{id}", (request, response, pathParams) -> {
                    String bookingId = pathParams.get("id");
                    bookingHandler.updateBooking(request, response, bookingId);
                })  // Update a booking by ID
                .addHandler(Method.DELETE, "/book/{id}", (request, response, pathParams) -> {
                    String bookingId = pathParams.get("id");
                    bookingHandler.deleteBooking(request, response, bookingId);
                })  // Delete a booking by ID
                .start();

        System.out.println("Server started at " + server.uri());

        // Shutdown hook to stop the server gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    }
}