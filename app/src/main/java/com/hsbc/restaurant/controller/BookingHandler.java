package com.hsbc.restaurant.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.restaurant.model.Booking;
import com.hsbc.restaurant.service.BookingService;
import io.muserver.MuRequest;
import io.muserver.MuResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class BookingHandler {

    private final BookingService bookingService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization

    // Constructor to initialize the BookingService
    public BookingHandler() {
        this.bookingService = new BookingService();
    }

    // Create a new booking (POST /book)
    // Create a new booking (POST /book)
    public void createBooking(MuRequest request, MuResponse response) throws IOException {
        try {

            // Parse the request body as JSON
            JsonNode requestBody = objectMapper.readTree(request.readBodyAsString());

            // Extract customer and booking information
            JsonNode customerNode = requestBody.get("customer");
            JsonNode bookingNode = requestBody.get("booking");

            if (customerNode == null || bookingNode == null) {
                sendJsonError(response, 400, "Invalid request. 'customer' and 'booking' are required.");
                return;
            }

            // Validate customer data
            Integer customerId = customerNode.hasNonNull("customer_id") ? customerNode.get("customer_id").asInt() : null;
            String name = customerNode.hasNonNull("name") ? customerNode.get("name").asText() : null;
            String email = customerNode.hasNonNull("email") ? customerNode.get("email").asText() : null;
            String phoneNumber = customerNode.hasNonNull("phone_number") ? customerNode.get("phone_number").asText() : null;

            if (customerId == null && (name == null || email == null || phoneNumber == null)) {
                sendJsonError(response, 400, "Missing required customer information.");
                return;
            }

            // Validate booking data
            String bookingDate = bookingNode.get("booking_date").asText();
            String bookingTime = bookingNode.get("booking_time").asText();
            int tableSize = bookingNode.get("table_size").asInt();

            if (bookingDate == null || bookingTime == null || tableSize <= 0) {
                sendJsonError(response, 400, "Missing required booking information.");
                return;
            }

            // If customer_id is null, create a new customer
            if (customerId == null) {
                customerId = bookingService.createCustomer(name, email, phoneNumber);
                if (customerId == null) {
                    sendJsonError(response, 500, "Error creating customer.");
                    return;
                }
            }

            // Create the booking
            int bookingId = bookingService.createBooking(customerId, bookingDate, bookingTime, tableSize);
            sendJsonResponse(response, 200, bookingId, "Booking created successfully.");


        } catch (SQLException e) {
            sendJsonError(response, 500, "Error creating booking: " + e.getMessage());
        } catch (Exception e) {
            sendJsonError(response, 400, "Invalid request format: " + e.getMessage());
        }
    }

    // Retrieve all bookings (GET /bookings)
    public void getAllBookings(MuRequest request,
                               MuResponse response) throws IOException {
        getAllBookings(request, response, "");
    }

    // Retrieve all bookings (GET /bookings)
    public void getAllBookings(MuRequest request, MuResponse response, String dateString) throws IOException {
        try {
            // Retrieve the bookings from the service
            List<Booking> bookings = bookingService.getAllBookings(dateString);

            // Convert the bookings list to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(bookings);

            // Set the content type to JSON and write the JSON response
            response.contentType("application/json");
            response.write(jsonResponse);
        } catch (SQLException e) {
            // Handle SQL exceptions and return an error message in JSON format
            sendJsonError(response, 500, "Error retrieving bookings: " + e.getMessage());
        }
    }

    // Retrieve a booking by ID (GET /book/{id})
    public void getBookingById(MuRequest request, MuResponse response, String bookingId) throws IOException {
        try {
            String bookingDetails = bookingService.getBookingById(Integer.parseInt(bookingId));
            response.write(bookingDetails);
        } catch (SQLException | NumberFormatException e) {
            response.status(500);
            response.write("Error retrieving booking: " + e.getMessage());
        }
    }

    // Update a booking (PUT /book/{id})
    public void updateBooking(MuRequest request, MuResponse response, String bookingId) throws IOException {
        try {
            int customerId = Integer.parseInt(request.form().get("customerId"));
            String bookingDate = request.form().get("bookingDate");
            String bookingTime = request.form().get("bookingTime");
            int tableSize = Integer.parseInt(request.form().get("tableSize"));

            boolean success = bookingService.updateBooking(Integer.parseInt(bookingId), customerId, bookingDate, bookingTime, tableSize);
            if (success) {
                response.write("Booking updated successfully.");
            } else {
                response.write("Failed to update booking.");
            }
        } catch (SQLException | NumberFormatException e) {
            response.status(500);
            response.write("Error updating booking: " + e.getMessage());
        }
    }

    // Delete a booking (DELETE /book/{id})
    public void deleteBooking(MuRequest request, MuResponse response, String bookingId) throws IOException {
        try {
            boolean success = bookingService.deleteBooking(Integer.parseInt(bookingId));
            if (success) {
                response.write("Booking deleted successfully.");
            } else {
                response.write("Failed to delete booking.");
            }
        } catch (SQLException | NumberFormatException e) {
            response.status(500);
            response.write("Error deleting booking: " + e.getMessage());
        }
    }

    // Utility method to send JSON error responses
    private void sendJsonError(MuResponse response, int statusCode, String message) throws IOException {
        response.status(statusCode);
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", message);
        response.write(objectMapper.writeValueAsString(errorResponse));
    }

    // Utility method to send JSON success responses
    private void sendJsonResponse(MuResponse response, int statusCode, int bookingId, String message) throws IOException {
        response.status(statusCode);
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("status", "success");
        successResponse.put("booking_id", bookingId);
        successResponse.put("message", message);
        response.write(objectMapper.writeValueAsString(successResponse));
    }
}
