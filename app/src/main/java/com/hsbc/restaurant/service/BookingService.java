package com.hsbc.restaurant.service;

import com.hsbc.db.ConnectionPool;
import com.hsbc.restaurant.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingService {

    // Create a new booking
    public Integer createBooking(int customerId, String bookingDate, String bookingTime, int tableSize) throws SQLException {
        String query = "INSERT INTO booking (customer_id, booking_date, booking_time, table_size) VALUES (?, ?, ?, ?)";

        // Use try-with-resources to manage resources automatically
        try (
                java.sql.Connection conn = ConnectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            // Set parameters for the query
            pstmt.setInt(1, customerId);
            pstmt.setString(2, bookingDate);
            pstmt.setString(3, bookingTime);
            pstmt.setInt(4, tableSize);

            int affectedRows = pstmt.executeUpdate();  // Execute the insert query

            // Check if the insert was successful
            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed, no rows affected.");
            }

            // Retrieve the generated booking_id
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);  // Return the generated booking_id
                } else {
                    throw new SQLException("Creating booking failed, no ID obtained.");
                }
            }
        }
    }

    // Retrieve all bookings
    public List<Booking> getAllBookings(String dateString) throws SQLException {
        String query = "SELECT * FROM booking WHERE booking_date = ?";
        List<Booking> bookings = new ArrayList<>();

        try (
                java.sql.Connection conn = ConnectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
            pstmt.setString(1, dateString);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Create a new Booking object for each record in the result set
                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("customer_id"),
                        rs.getString("booking_date"),
                        rs.getString("booking_time"),
                        rs.getInt("table_size")
                );

                // Add the Booking object to the list
                bookings.add(booking);
            }
        }
        return bookings;  // Return the list of Booking objects
    }

    // Retrieve a booking by ID
    public String getBookingById(int bookingId) throws SQLException {
        String query = "SELECT * FROM booking WHERE booking_id = ?";
        try (
                java.sql.Connection conn = ConnectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return "Booking ID: " + rs.getInt("booking_id") + ", Customer ID: " + rs.getInt("customer_id")
                            + ", Date: " + rs.getString("booking_date") + ", Time: " + rs.getString("booking_time")
                            + ", Table Size: " + rs.getInt("table_size");
                }
            }
        }
        return "No booking found with ID: " + bookingId;
    }

    // Update an existing booking
    public boolean updateBooking(int bookingId, int customerId, String bookingDate, String bookingTime, int tableSize) throws SQLException {
        String query = "UPDATE booking SET customer_id = ?, booking_date = ?, booking_time = ?, table_size = ? WHERE booking_id = ?";
        try (
                java.sql.Connection conn = ConnectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
            pstmt.setInt(1, customerId);
            pstmt.setString(2, bookingDate);
            pstmt.setString(3, bookingTime);
            pstmt.setInt(4, tableSize);
            pstmt.setInt(5, bookingId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Delete a booking
    public boolean deleteBooking(int bookingId) throws SQLException {
        String query = "DELETE FROM booking WHERE booking_id = ?";
        try (
                java.sql.Connection conn = ConnectionPool.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)
        ) {
            pstmt.setInt(1, bookingId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public Integer createCustomer(String name, String email, String phoneNumber) throws SQLException {
        String selectQuery = "SELECT customer_id FROM customer WHERE email = ?";
        String insertQuery = "INSERT INTO customer (name, email, phone_number) VALUES (?, ?, ?)";

        // Try-with-resources to automatically close resources
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {

            // Check if the customer already exists by email
            selectStmt.setString(1, email);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    // If a customer with this email exists, return the existing customer_id
                    return rs.getInt("customer_id");
                }
            }

            // If no existing customer found, insert a new one
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, name);
                insertStmt.setString(2, email);
                insertStmt.setString(3, phoneNumber);

                int affectedRows = insertStmt.executeUpdate();  // Execute the insert query

                // Check if the insert was successful
                if (affectedRows == 0) {
                    throw new SQLException("Creating customer failed, no rows affected.");
                }

                // Retrieve the generated customer_id
                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);  // Return the generated customer_id
                    } else {
                        throw new SQLException("Creating customer failed, no ID obtained.");
                    }
                }
            }
        }
    }


}
